package com.enach.ServiceIMPL;


import com.enach.Entity.CustomerDetails;
import com.enach.Entity.EnachPayment;
import com.enach.Entity.OtpDetails;
import com.enach.Models.*;
import com.enach.Repository.BranchDetailRepository;
import com.enach.Repository.CustomerDetailsRepository;
import com.enach.Repository.EnachPaymentRepository;
import com.enach.Repository.OtpDetailsRepository;
import com.enach.Service.CoustomerService;
import com.enach.Service.DatabaseService;
import com.enach.Utill.CustomerDetailsUtility;
import com.enach.Utill.OtpUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;


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

    @Override
    public HashMap<String, String> validateCustAndSendOtp(String applicationNo) {

        HashMap<String, String> otpResponse = new HashMap<>();

        try {
            List<CustomerDetails> listData =databaseService.getCustomerDetails(applicationNo);
            System.out.println("listData" + listData);
            if (!listData.isEmpty()) {
                CustomerDetails customerDetails = listData.get(0);

                if (!StringUtils.isEmpty(customerDetails.getPhoneNumber())) {
                    int otpCode = otpUtility.generateCustOtp(customerDetails);

                    if (otpCode > 0) {

                        System.out.println("otp generated successfully");
//                        if (otpUtility.sendOtp(customerDetails.getPhoneNumber(), otpCode, customerDetails.getApplicationNumber())) {
                        if (otpUtility.sendOtp("8160041657", otpCode, customerDetails.getApplicationNumber())) {

                            System.out.println("otp sent on mobile");
                            OtpDetails otpDetails = new OtpDetails();
                            otpDetails.setOtpCode((long) otpCode);
                            System.out.println(otpCode);

                            otpDetails.setMobileNo(customerDetails.getPhoneNumber());

                            otpDetailsRepository.save(otpDetails);
                            System.out.println("otp save successfully");
                            Long otpId = otpDetails.getOtpId();
//                            otpResponse.put("otpCode", String.valueOf(otpCode));
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
    public EnachPayment updateEnachPaymentStatus(String transactionNo, String transactionStatus, String errorMessage) {

        EnachPayment enachPayment = null;

        try {
            enachPayment = enachPaymentRepository.findByTansactionNo(transactionNo);

            if (enachPayment != null && !StringUtils.isEmpty(enachPayment)) {

                Timestamp transactionCompleteDate = new Timestamp(System.currentTimeMillis());
                enachPaymentRepository.updatePaymentStatus(transactionNo, transactionStatus, errorMessage, transactionCompleteDate);
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

            EnachPayment paymentDetail = enachPaymentRepository.findLoanNoAndMandateType(transactionNo);
            mandateType=paymentDetail.getMandateType();
            applicationNo=paymentDetail.getApplicationNo();

            //===========================WHEN Email Details get from DB then open this code ==============================
           BranchNameDetail branchNameDetailDetails= databaseService.branchName(applicationNo);

            if (branchNameDetailDetails!=null) {

            String branchName = branchNameDetailDetails.getBranchName();
            String emailId = branchDetailRepository.findByBranchEmail(branchName);
            System.out.println("BranchEmail " + emailId);

//            String emailId = "ravi.soni@shubham.co";
                EmailDetails emailDetails = new EmailDetails();

                if ("Success".equalsIgnoreCase(transactionStatus)) {
                    emailDetails.setRecipient(emailId);
                    emailDetails.setSubject("E-NACH SHUBHAM");
                    emailDetails.setMsgBody("Enach registration has been successfully completed \n" + "for " + mandateType + " to ApplicationNo " + applicationNo + " and TransactionNo " + transactionNo + ".\n" + "Regards\n" + "Shubham Housing Development Finance Company");

                    otpUtility.sendSimpleMail(emailDetails);

                } else if ("Failed".equalsIgnoreCase(transactionStatus)) {
                    emailDetails.setRecipient(emailId);
                    emailDetails.setSubject("E-NACH SHUBHAM");
                    emailDetails.setMsgBody("Enach registration has been failed \n" + "due to " + errorMessage + " for " + mandateType + " to ApplicationNo " + applicationNo + " and TransactionNo " + transactionNo + ".\n" + "Regards\n" + "Shubham Housing Development Finance Company");

                    otpUtility.sendSimpleMail(emailDetails);
                }
            } else {
                System.out.println("emailId does not exist.");
            }
        } catch (Exception e) {
            System.out.println(transactionNo + " error is " + e);
        }
    }

}
