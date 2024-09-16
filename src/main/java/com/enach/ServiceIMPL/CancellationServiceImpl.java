package com.enach.ServiceIMPL;

import com.enach.Entity.CustomerDetails;
import com.enach.Entity.EmailDetails;
import com.enach.Entity.OtpDetails;
import com.enach.Entity.StatusManage;
import com.enach.Models.SaveStatusRequest;
import com.enach.Repository.EmailDetailsRepo;
import com.enach.Repository.OtpDetailsRepository;
import com.enach.Repository.StatusRepository;
import com.enach.Service.CancellationService;
import com.enach.Service.DatabaseService;
import com.enach.Utill.SendEmailUtility;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class CancellationServiceImpl implements CancellationService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;
    @Autowired
    private OtpDetailsRepository otpDetailsRepository;

    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private SendEmailUtility sendEmailUtility;
    @Autowired
    private EmailDetailsRepo emailDetailsRepo;

    private final Logger logger = LoggerFactory.getLogger(CancellationServiceImpl.class);

    public List<CustomerDetails> getCustomerDetail(String mobileNo, String otpCode, String applicationNo) {

        try {
            OtpDetails otpDetails = otpDetailsRepository.IsotpExpired(mobileNo, otpCode);
            if (otpDetails != null) {

                Duration duration = Duration.between(otpDetails.getOtpExprTime(), LocalDateTime.now());
               List<CustomerDetails> listData = (duration.toMinutes() > 10) ? null : databaseService.getCustomerDetailsFromLoans(applicationNo);
                return listData;
            } else {
                logger.info("Otp-code does not exist for {} {}", mobileNo, otpCode);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error while validating otp-code for {}", applicationNo);
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

    @Scheduled(cron = "0 0 20 * * *")
    public void generateReportForCancellation() {
        try {
            List<StatusManage> statusManage = statusRepository.findAll();
            logger.info("Cancellation record for mis report {}",statusManage.size());
            if (!statusManage.isEmpty()) {
                generateCancellationExcel(statusManage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateCancellationExcel(List<StatusManage> statusManageList) {

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("status-Details");
            int rowCount = 0;

            String[] header = {"Application Number", "Loan Number", "Status", "Cancellation Time"};
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
            List<EmailDetails> emailDetails = emailDetailsRepo.findAll();
            for (EmailDetails reciver : emailDetails) {
                if(reciver.getReportType().contains("cancellation")) {
                    sendEmailUtility.sendCancellationAttachment(excelData, reciver.getEmail());
                    emailDetailsRepo.updateSendingTime(reciver.getEmail(), Timestamp.valueOf(LocalDateTime.now()));

                }
            }

        } catch (Exception e) {
            System.out.println("Error while executing report query :" + e.getMessage());
        }
    }


}
