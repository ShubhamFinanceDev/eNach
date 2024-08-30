package com.enach.ServiceIMPL;

import com.enach.Entity.CustomerDetails;
import com.enach.Entity.OtpDetails;
import com.enach.Entity.StatusManage;
import com.enach.Models.SaveStatusRequest;
import com.enach.Repository.OtpDetailsRepository;
import com.enach.Repository.StatusRepository;
import com.enach.Service.CancellationService;
import com.enach.Service.DatabaseService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CancellationServiceImpl implements CancellationService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;
    @Value("${spring.mail.reciver}")
    private String reciver;
    @Autowired
    private OtpDetailsRepository otpDetailsRepository;

    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private StatusRepository statusRepository;

    private final Logger logger = LoggerFactory.getLogger(CancellationServiceImpl.class);

    public List<CustomerDetails> getCustomerDetail(String mobileNo, String otpCode, String applicationNo) {
        try {
            // Fetch list of customer details based on application number
            List<CustomerDetails> listData = databaseService.getCustomerDetailsFromLoans(applicationNo);
            return listData;
        } catch (Exception e) {
            System.out.println("Error fetching customer details: " + e.getMessage());
            e.printStackTrace();  // Log the full stack trace
            return null;
        }
    }

    @Override
    public String statusRequest(SaveStatusRequest statusRequest) throws Exception {

        StatusManage statusManage = new StatusManage();

        statusManage.setApplicationNo(statusRequest.getApplicationNo());
        statusManage.setCancelCause(statusRequest.getCancelCause());
        statusManage.setLoanNo(statusRequest.getLoanNo());
        statusManage.setCancellationTime(Timestamp.valueOf(LocalDateTime.now()));

        statusRepository.save(statusManage);

        return "Cancel status save successfully";
    }

    @Scheduled(cron = "0 52 16 * * ?")
    public ResponseEntity<?> generateReportForOfStatus() throws Exception {
        try {
            List<StatusManage> statusManage = statusRepository.findAll();
            if (statusManage.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            generateExcel(statusManage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void generateExcel(List<StatusManage> statusManageList) {

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("status-Details");
            int rowCount = 0;

            String[] header = {"Application Number", "Loan Number", "Cancellation Cause", "Cancellation Time"};
            Row headerRow = sheet.createRow(rowCount++);
            int cellCount = 0;

            for (String headerValue : header) {
                headerRow.createCell(cellCount++).setCellValue(headerValue);
            }
            for (StatusManage list : statusManageList) {
                Row row = sheet.createRow(rowCount++);
                row.createCell(0).setCellValue(list.getApplicationNo());
                row.createCell(1).setCellValue(list.getLoanNo());
                row.createCell(2).setCellValue(list.getCancelCause());
                row.createCell(3).setCellValue(list.getCancellationTime().toString());
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();
            byte[] excelData = outputStream.toByteArray();
            sendEmailWithAttachment(excelData);

        } catch (Exception e) {
            System.out.println("Error while executing report query :" + e.getMessage());
        }
    }

    private void sendEmailWithAttachment(byte[] excelData) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(sender);
        helper.setTo(reciver);
        helper.setText("Dear Sir, \n\n\n Please find the below attached sheet. \n\n\n\n Regards\n It Support.");
        helper.setSubject("Cancellation mail");

        InputStreamSource attachmentSource = new ByteArrayResource(excelData);
        helper.addAttachment("Cancellation-status-report.xlsx", attachmentSource);
        mailSender.send(message);
        logger.info("Generate excel and send to mail {}", reciver);
    }
}
