package com.enach.ServiceIMPL;


import com.enach.Entity.CustomerDetails;
import com.enach.Entity.EnachPayment;
import com.enach.Entity.OtpDetails;
import com.enach.Models.*;
import com.enach.Repository.*;
import com.enach.Service.CoustomerService;
import com.enach.Service.DatabaseService;
import com.enach.Utill.CustomerDetailsUtility;
import com.enach.Utill.OtpUtility;
import com.enach.Utill.SendEmailUtility;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

@EnableScheduling
@Service
public class CustomerServiceIMPL implements CoustomerService {


    @Autowired
    @Qualifier("jdbcJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private OtpUtility otpUtility;
    @Autowired
    private OtpDetailsRepository otpDetailsRepository;
    @Autowired
    private EnachPaymentRepository enachPaymentRepository;
    @Autowired
    private CustomerDetailsUtility customerDetailsUtility;
    @Autowired
    private BranchDetailRepository branchDetailRepository;
    @Autowired
    private CustomerDetailsRepository customerDetailsRepository;
    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private SendEmailUtility sendEmailUtility;
    @Autowired
    private EmailDetailsRepo emailDetailsRepo;

    private final Logger logger = LoggerFactory.getLogger(CustomerServiceIMPL.class);

    @Override
    public HashMap<String, String> validateCustAndSendOtp(String applicationNo) {

        HashMap<String, String> otpResponse = new HashMap<>();

        try {
            List<CustomerDetails> listData = databaseService.getCustomerDetails(applicationNo);
            System.out.println("listData" + listData);
            if (!listData.isEmpty()) {
                CustomerDetails customerDetails = listData.get(0);

                if (!StringUtils.isEmpty(customerDetails.getPhoneNumber())) {
                    int otpCode = otpUtility.generateCustOtp(customerDetails);

                    if (otpCode > 0) {

                        System.out.println("otp generated successfully");
                        if (otpUtility.sendOtp(customerDetails.getPhoneNumber(), otpCode, customerDetails.getApplicationNumber())) {
//                        if (otpUtility.sendOtp("8160041657", otpCode, customerDetails.getApplicationNumber())) {

                            System.out.println("otp sent on mobile");
                            OtpDetails otpDetails = new OtpDetails();
                            otpDetails.setOtpCode((long) otpCode);
                            System.out.println(otpCode);

                            otpDetails.setMobileNo(customerDetails.getPhoneNumber());

                            otpDetailsRepository.save(otpDetails);
                            System.out.println("otp save successfully");
                            Long otpId = otpDetails.getOtpId();
                            otpResponse.put("otpCode", String.valueOf(otpCode));
//                            otpResponse.put("otpId", String.valueOf(otpId));
                            otpResponse.put("mobile", otpDetails.getMobileNo());
                            otpResponse.put("msg", "Otp send.");
                            otpResponse.put("code", "0000");

                        } else {
                            otpResponse.put("msg", "Otp did not send, please try again");
                            otpResponse.put("code", "1111");
                        }

                    } else {
                        otpResponse.put("msg", "Otp did not generated, please try again");
                        otpResponse.put("code", "1111");
                    }

                } else {
                    otpResponse.put("msg", "mobile no does not exist with this application no.");
                    otpResponse.put("code", "1111");
                }
            } else {
                otpResponse.put("msg", "Application no does not exist / deactivate.");
                otpResponse.put("code", "1111");
            }

        } catch (Exception e) {
            otpResponse.put("msg", "something went worng");
            otpResponse.put("code", "1111");
            System.out.println(e);
        }
        return otpResponse;
    }


    @Override
    public CustomerDetails getCustomerDetail(String mobileNo, String otpCode, String applicationNo) {

        CustomerDetails customerDetails = new CustomerDetails();
        try {
            OtpDetails otpDetails = otpDetailsRepository.IsotpExpired(mobileNo, otpCode);
            if (otpDetails != null) {

                List<CustomerDetails> listData = databaseService.getCustomerDetails(applicationNo);
                if (!listData.isEmpty()) {
                    customerDetails = listData.get(0);
                } else {
                    customerDetails = null;
                }

                Duration duration = Duration.between(otpDetails.getOtpExprTime(), LocalDateTime.now());
                customerDetails = (duration.toMinutes() > 5) ? null : customerDetails;
            } else {
                customerDetails = null;
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return customerDetails;
    }


    @Override
    public EnachPayment updateEnachPaymentStatus(String transactionNo, String transactionStatus, String errorMessage, String refrenceId, String umrn) {

        EnachPayment enachPayment = null;

        try {
            enachPayment = enachPaymentRepository.findByTansactionNo(transactionNo);

            if (enachPayment != null && !StringUtils.isEmpty(enachPayment)) {

                Timestamp transactionCompleteDate = new Timestamp(System.currentTimeMillis());
                enachPaymentRepository.updatePaymentStatus(transactionNo, transactionStatus, errorMessage, transactionCompleteDate, refrenceId, umrn);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return enachPayment;
    }

    @Async
    @Override
    public void sendEmailOnBank(String transactionNo, String transactionStatus, String errorMessage) {

        try {

            String mandateType = "";
            String applicationNo = "";
            String registeredAmount = "";

            EnachPayment paymentDetail = enachPaymentRepository.findLoanNoAndMandateType(transactionNo);
            mandateType = paymentDetail.getMandateType();
            applicationNo = paymentDetail.getApplicationNo();
            registeredAmount = paymentDetail.getRegisteredAmount();

            //===========================WHEN Email Details get from DB then open this code ==============================
            BranchNameDetail branchNameDetailDetails = databaseService.branchName(applicationNo);
//            String emailId = branchDetailRepository.findByBranchEmail(branchNameDetailDetails.getBranchName());
            String emailId = "apps.development@shubham.co";
            if (emailId != null) {

                logger.info("BranchEmail of {} {}", branchNameDetailDetails.getBranchName(), emailId);
                EmailDetails emailDetails = new EmailDetails();

                if ("Success".equalsIgnoreCase(transactionStatus)) {
                    emailDetails.setRecipient(emailId);
                    emailDetails.setSubject("application number");
                    emailDetails.setMsgBody("Dear Sir/Mam,\n\n\n" +
                            "E-NACH registration has been successfully completed for " + mandateType +
                            " with Application No: " + applicationNo +
                            " and a registered amount of " + registeredAmount + ".\n\n\n\n\n" +
                            "Regards,\n" +
                            "Shubham Housing Finance.");

                    otpUtility.sendSimpleMail(emailDetails);
                } else if ("Failed".equalsIgnoreCase(transactionStatus)) {
                    emailDetails.setRecipient(emailId);
                    emailDetails.setSubject("application number");
                    emailDetails.setMsgBody("Dear Sir/Mam,\n\n\n" +
                            "E-NACH registration has failed due to " + errorMessage +
                            " for " + mandateType +
                            " with Application No: " + applicationNo +
                            " and a registered amount of " + registeredAmount + ".\n\n\n\n\n" +
                            "Regards,\n" +
                            "Shubham Housing Finance.");

                    otpUtility.sendSimpleMail(emailDetails);
                }
                logger.info("Acknowledgement mail has been sent successfully.");

            } else {
                System.out.println("emailId does not exist.");
            }
        } catch (Exception e) {
            System.out.println(transactionNo + " error is " + e);
        }
    }

    @Override
    @Scheduled(cron = "0 0 9-23/2 * * *") // every 2 hours
    public void generateReportOnMail() {
        logger.info("Generating report on mail process invoke at {}", LocalDateTime.now());
        try {
            List<EnachPayment> enachPayment = enachPaymentRepository.findByTransactionStatus();
            logger.info("Row fetched in payment-records {}", enachPayment.size());
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Enach-payment-report");
            int rowCount = 0;

            String[] header = {"Transaction-No", "Application-No", "Payment-Method", "Transaction-Start-Date", "Transaction-Complete-Date", "Transaction-Status", "Mandate-Type", "Error-Message", "Amount", "Refrence-Id", "Bank-Name", "Account-No", "Start-Date", "End-Date", "Ifsc-Code", "Umrn-No"};
            Row headerRow = sheet.createRow(rowCount++);
            int cellCount = 0;

            for (String headerValue : header) {
                headerRow.createCell(cellCount++).setCellValue(headerValue);
            }
            for (EnachPayment details : enachPayment) {
                Row row = sheet.createRow(rowCount++);
                row.createCell(0).setCellValue(details.getTransactionNo() != null ? details.getTransactionNo() : "");
                row.createCell(1).setCellValue(details.getApplicationNo() != null ? details.getApplicationNo() : "");
                row.createCell(2).setCellValue(details.getPaymentMethod() != null ? details.getPaymentMethod() : "");
                row.createCell(3).setCellValue(details.getTransactionStartDate() != null ? details.getTransactionStartDate().toString() : "");
                row.createCell(4).setCellValue(details.getTransactionCompleteDate() != null ? details.getTransactionCompleteDate().toString() : "");
                row.createCell(5).setCellValue(details.getTransactionStatus() != null ? details.getTransactionStatus() : "");
                row.createCell(6).setCellValue(details.getMandateType() != null ? details.getMandateType() : "");
                row.createCell(7).setCellValue(details.getErrorMessage() != null ? details.getErrorMessage() : "");
                row.createCell(8).setCellValue(details.getAmount() != null ? details.getAmount().toString() : "");
                row.createCell(9).setCellValue(details.getRefrenceId() != null ? details.getRefrenceId() : "");
                row.createCell(10).setCellValue(details.getBankName() != null ? details.getBankName() : "");
                row.createCell(11).setCellValue(details.getBankAccountNo() != null ? details.getBankAccountNo() : "");
                row.createCell(12).setCellValue(details.getStartDate() != null ? details.getStartDate().toString() : "");
                row.createCell(13).setCellValue(details.getEndDate() != null ? details.getEndDate().toString() : "");
                row.createCell(14).setCellValue(details.getIfscCode() != null ? details.getIfscCode() : "");
                row.createCell(15).setCellValue(details.getUmrn() != null ? details.getUmrn() : "");
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();
            byte[] excelData = outputStream.toByteArray();
            logger.info("Report created.");
            List<com.enach.Entity.EmailDetails> emailDetails = emailDetailsRepo.findAll();
            for (com.enach.Entity.EmailDetails emails : emailDetails) {
                String email = emails.getEmail();
                sendEmailUtility.sendEmailWithAttachment(email, excelData);
                emailDetailsRepo.updateSendingTime(email, Timestamp.valueOf(LocalDateTime.now()));
            }
            logger.info("Report has been shared. No of email {}", emailDetails.size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}