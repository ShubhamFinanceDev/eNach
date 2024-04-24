package com.enach.ServiceIMPL;


import com.enach.Entity.EnachPayment;
import com.enach.Entity.OtpDetails;
import com.enach.Models.*;
import com.enach.Repository.EnachPaymentRepository;
import com.enach.Repository.OtpDetailsRepository;
import com.enach.Service.CoustomerService;
import com.enach.Utill.OtpUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

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


    Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);


    @Override
    public HashMap<String, String> validateCustAndSendOtp(String applicationNo) {

        HashMap<String, String> otpResponse = new HashMap<>();
        //String sql = "SELECT * FROM enach WHERE `Application Number`='"+applicationNo+"'";

    try {

        String sql= " SELECT * FROM (SELECT a.`Application NUMBER`, a.`Branch NAME`, a.`Sanction Amount`,a.`Customer NUMBER`,\n"
                + " l.`CUSTOMER NAME`, a.`First Disbursal DATE`, a.`First Instalment DATE`, l.`Installment Amount`,\n"
                + " l.`NEXT DUE DATE`, a.`Current STATUS`, c.`Mobile No`,l.`LOAN STATUS`\n"
                + " FROM application a\n"
                + " left JOIN  loandetails l ON a.`Application Number`=l.CASAPPLNO\n"
                + " left  JOIN communication c ON a.`Customer Number`=c.`Customer Number`\n"
                + " UNION\n"
                + " SELECT e.APPLICATION_NUMBER,e.BRANCH_NAME,e.SANCTION_AMOUNT,NULL,e.CUSTOMER_NAME,DATE_FORMAT(e.FIRST_DISBURSAL_DATE,'%Y-%m-%d') AS FIRST_DISBURSAL_DATE,\n"
                + " DATE_FORMAT(e.FIRST_INSTALLMENT_DATE,'%Y-%m-%d') AS FIRST_INSTALLMENT_DATE, CAST(e.INSTALLMENT_AMOUNT as DECIMAL(25,2)) AS INSTALLMENT_AMOUNT,\n"
                + " DATE_FORMAT(e.NEXT_DUE_DATE,'%d-%m-%y') AS NEXT_DUE_DATE,e.CURRENT_STATUS,e.Mobile_No ,l.`LOAN STATUS`\n"
                + " FROM enach_old	e\n"
                + " left JOIN  loandetails l ON l.`LOAN NO`=e.APPLICATION_NUMBER\n"
                + " ) a WHERE a.`LOAN STATUS`='A' AND a.`Application Number` LIKE '"+applicationNo+"' \n";

        List<CustomerDetails> listData = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CustomerDetails.class));

        if (!listData.isEmpty() && listData.size() > 0) {
            CustomerDetails customerDetails = listData.get(0);

          if(!StringUtils.isEmpty(customerDetails.getMobileNo())) {
            int otpCode = otpUtility.generateCustOtp(customerDetails);

            if (otpCode > 0) {

                logger.info("otp generated successfully");
                //   if (otpUtility.sendOtp(customerDetails.getMobileNo(), otpCode)) {
                logger.info("otp sent on mobile");

                OtpDetails otpDetails = new OtpDetails();
                otpDetails.setOtpCode(Long.valueOf(otpCode));

                System.out.println(otpCode);

                otpDetails.setMobileNo(customerDetails.getMobileNo());

                otpDetailsRepository.save(otpDetails);
                Long otpId = otpDetails.getOtpId();
                otpResponse.put("otpCode", String.valueOf(otpCode));
                otpResponse.put("otpId", String.valueOf(otpId));
                otpResponse.put("mobile", otpDetails.getMobileNo());
                otpResponse.put("msg", "Otp send.");
                otpResponse.put("code", "0000");

                //     } else {
                //         otpResponse.put("msg", "Otp did not send, please try again");
                //         otpResponse.put("code", "1111");
                //     }

            } else {
                otpResponse.put("msg", "Otp did not generated, please try again");
                otpResponse.put("code", "1111");
            }

          }else{
              otpResponse.put("msg", "mobile no does not exist with this application no.");
              otpResponse.put("code", "1111");
          }
        } else {
            otpResponse.put("msg", "Application no does not exist / deactivate");
            otpResponse.put("code", "1111");
        }

    } catch (Exception e) {
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

                String sql= " SELECT * FROM (SELECT a.`Application NUMBER`, a.`Branch NAME`, a.`Sanction Amount`,a.`Customer NUMBER`,\n"
                        + " l.`CUSTOMER NAME`, a.`First Disbursal DATE`, a.`First Instalment DATE`, l.`Installment Amount`,\n"
                        + " l.`NEXT DUE DATE`, a.`Current STATUS`, c.`Mobile No`,l.`LOAN STATUS`\n"
                        + " FROM application a\n"
                        + " left JOIN  loandetails l ON a.`Application Number`=l.CASAPPLNO\n"
                        + " left  JOIN communication c ON a.`Customer Number`=c.`Customer Number`\n"
                        + " UNION\n"
                        + " SELECT e.APPLICATION_NUMBER,e.BRANCH_NAME,e.SANCTION_AMOUNT,NULL,e.CUSTOMER_NAME,DATE_FORMAT(e.FIRST_DISBURSAL_DATE,'%Y-%m-%d') AS FIRST_DISBURSAL_DATE,\n"
                        + " DATE_FORMAT(e.FIRST_INSTALLMENT_DATE,'%Y-%m-%d') AS FIRST_INSTALLMENT_DATE, CAST(e.INSTALLMENT_AMOUNT as DECIMAL(25,2)) AS INSTALLMENT_AMOUNT,\n"
                        + " DATE_FORMAT(e.NEXT_DUE_DATE,'%d-%m-%y') AS NEXT_DUE_DATE,e.CURRENT_STATUS,e.Mobile_No ,l.`LOAN STATUS`\n"
                        + " FROM enach_old	e\n"
                        + " left JOIN  loandetails l ON l.`LOAN NO`=e.APPLICATION_NUMBER\n"
                        + " ) a WHERE a.`LOAN STATUS`='A' AND a.`Application Number` LIKE '"+applicationNo+"' \n";

                List<CustomerDetails> listData = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(CustomerDetails.class));

                if(!listData.isEmpty() && listData.size()>0) {
                    customerDetails = listData.get(0);
                }else{
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
                enachPaymentRepository.updatePaymentStatus(transactionNo, transactionStatus,errorMessage,transactionCompleteDate);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return enachPayment;
    }


     @Override
    public void sendEmailOnBank(String transactionNo, String transactionStatus,String errorMessage) {

        BranchDetails branchDetails = new BranchDetails();
        CommonResponse commonResponse = new CommonResponse();
        try {

           String mandateType = "";
           String applicationNo = "";

           List<?> dataList = enachPaymentRepository.findLoanNoAndMandateType(transactionNo);

           if(!dataList.isEmpty()) {
            Object[] obj = (Object[]) dataList.get(0);
            mandateType = ""+obj[0];
            applicationNo = ""+obj[1];
           }

         /* String sql = "SELECT enach.Branch_Name,branch_detail.Email_Id FROM enach_customer_details.enach  INNER JOIN \n"+
                      " enach_request_structure_details.branch_detail ON enach.Branch_Name = branch_detail.Branch_Name WHERE Application_Number='"+applicationNo+"' || Old_Application_Number='"+applicationNo+"' ";

            List<BranchDetails> listData = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(BranchDetails.class));
            if(!listData.isEmpty() && listData.size()>0) {
                branchDetails = listData.get(0);
             }
            String emailId = branchDetails.getEmailId();*/
          //  String emailId = "abhialok@5499@gmail.com";
            String emailId = "ravi.soni@shubham.co";

            if(!StringUtils.isEmpty(emailId)) {
                EmailDetails emailDetails = new EmailDetails();

                if ("Success".equalsIgnoreCase(transactionStatus)) {
                    emailDetails.setRecipient(emailId);
                    emailDetails.setSubject("E-NACH SHUBHAM");
                    emailDetails.setMsgBody("Enach registration has been successfully completed \n" +
                            "for " + mandateType + " to ApplicationNo " + applicationNo + " and TransactionNo " + transactionNo + ".\n" +
                            "Regards\n" +
                            "Shubham Housing Development Finance Company");

                    otpUtility.sendSimpleMail(emailDetails);

                } else if ("Failed".equalsIgnoreCase(transactionStatus)) {
                    emailDetails.setRecipient(emailId);
                    emailDetails.setSubject("E-NACH SHUBHAM");
                    emailDetails.setMsgBody("Enach registration has been failed \n" +
                            "due to " + errorMessage + " for " + mandateType + " to ApplicationNo " + applicationNo + " and TransactionNo " + transactionNo + ".\n" +
                            "Regards\n" +
                            "Shubham Housing Development Finance Company");

                    otpUtility.sendSimpleMail(emailDetails);
                }
            }else{
                System.out.println("emailId does not exist.");
            }
        }catch (Exception e){
            System.out.println(transactionNo+" error is "+e);
        }
    }

}
