package com.enach.ServiceIMPL;


import com.enach.Entity.EnachOldCustomerDetails;
import com.enach.Entity.EnachPayment;
import com.enach.Entity.OtpDetails;
import com.enach.Models.*;
import com.enach.Repository.EnachOldRepository;
import com.enach.Repository.EnachPaymentRepository;
import com.enach.Repository.OtpDetailsRepository;
import com.enach.Service.CoustomerService;
import com.enach.Utill.CustomerDetailsUtility;
import com.enach.Utill.OtpUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
    private EnachOldRepository enachOldRepository;


    Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);


    @Override
    public HashMap<String, String> validateCustAndSendOtp(String applicationNo) {

        HashMap<String, String> otpResponse = new HashMap<>();

    try {
        List<CustomerDetails> listData = new ArrayList<>();
        if(!applicationNo.contains("_")){

            /*String quary = "SELECT a.`Application NUMBER`, a.`Branch NAME`, a.`Sanction Amount`,a.`Customer NUMBER`,\n"
                    + " l.`CUSTOMER NAME`, a.`First Disbursal DATE`, a.`First Instalment DATE`, l.`Installment Amount`,\n"
                    + " l.`NEXT DUE DATE`, a.`Current STATUS`, c.`Mobile No`,l.`LOAN STATUS`\n"
                    + " FROM application a \n"
                    + "left JOIN  loandetails l ON a.`Application Number`=l.CASAPPLNO\n"
                    + "left  JOIN communication c ON a.`Customer Number`=c.`Customer Number`\n"
                    + "WHERE l.`LOAN STATUS`='A' AND a.`Application Number` LIKE  '"+applicationNo+"' \n";
*/
               String quary = customerDetailsUtility.getCustomerDetailsQuary(applicationNo);
              listData = jdbcTemplate.query(quary, new BeanPropertyRowMapper<>(CustomerDetails.class));

        }else{

           List<CustomerDetails> CustomerDetailsList = new ArrayList<>();
           List<?> list = enachOldRepository.findOldCustomerDetails(applicationNo);
           if (list.size()>0) {
              for (int i = 0; i < list.size(); i++) {

                CustomerDetails customerDetails = new CustomerDetails();
                Object object[] = (Object[]) list.get(i);

                customerDetails.setApplicationNumber(object[0]+"");
                customerDetails.setBranchName(object[1]+"");
                customerDetails.setSanctionAmount(Double.parseDouble(object[2]+""));
                customerDetails.setCustomerName(object[3]+"");
                customerDetails.setFirstDisbursalDate(LocalDate.parse(object[4]+""));
                customerDetails.setFirstInstalmentDate(LocalDate.parse(object[5]+""));
                customerDetails.setInstallmentAmount(Double.parseDouble(object[6]+""));
                customerDetails.setNextInstallmentDueDate(LocalDate.parse(object[7]+""));
                customerDetails.setMobileNo(object[8]+"");
                customerDetails.setCurrentStatus(object[9]+"");
                CustomerDetailsList.add(customerDetails);
           }
        }
         listData = CustomerDetailsList;
        }

            if (!listData.isEmpty() && listData.size() > 0) {
                CustomerDetails customerDetails = listData.get(0);

                if(!StringUtils.isEmpty(customerDetails.getMobileNo())) {
                    int otpCode = otpUtility.generateCustOtp(customerDetails);

                    if (otpCode > 0) {

                        logger.info("otp generated successfully");
                     //      if (otpUtility.sendOtp(customerDetails.getApplicationNumber(),customerDetails.getMobileNo(), otpCode)) {
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
                otpResponse.put("msg", "Application no does not exist / deactivate.");
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

                List<CustomerDetails> listData = new ArrayList<>();
                if(!applicationNo.contains("_")){

                   /* String quary = "SELECT a.`Application NUMBER`, a.`Branch NAME`, a.`Sanction Amount`,a.`Customer NUMBER`,\n"
                            + " l.`CUSTOMER NAME`, a.`First Disbursal DATE`, a.`First Instalment DATE`, l.`Installment Amount`,\n"
                            + " l.`NEXT DUE DATE`, a.`Current STATUS`, c.`Mobile No`,l.`LOAN STATUS`\n"
                            + " FROM application a \n"
                            + "left JOIN  loandetails l ON a.`Application Number`=l.CASAPPLNO\n"
                            + "left  JOIN communication c ON a.`Customer Number`=c.`Customer Number`\n"
                            + "WHERE l.`LOAN STATUS`='A' AND a.`Application Number` LIKE  '"+applicationNo+"' \n";
*/
                    String quary = customerDetailsUtility.getCustomerDetailsQuary(applicationNo);
                    listData = jdbcTemplate.query(quary, new BeanPropertyRowMapper<>(CustomerDetails.class));

                }else{

                    List<CustomerDetails> CustomerDetailsList = new ArrayList<>();
                    List<?> list = enachOldRepository.findOldCustomerDetails(applicationNo);
                    if (list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {

                            CustomerDetails customerDetails1 = new CustomerDetails();
                            Object object[] = (Object[]) list.get(i);

                            customerDetails.setApplicationNumber(object[0]+"");
                            customerDetails.setBranchName(object[1]+"");
                            customerDetails.setSanctionAmount(Double.parseDouble(object[2]+""));
                            customerDetails.setCustomerName(object[3]+"");
                            customerDetails.setFirstDisbursalDate(LocalDate.parse(object[4]+""));
                            customerDetails.setFirstInstalmentDate(LocalDate.parse(object[5]+""));
                            customerDetails.setInstallmentAmount(Double.parseDouble(object[6]+""));
                            customerDetails.setNextInstallmentDueDate(LocalDate.parse(object[7]+""));
                            customerDetails.setMobileNo(object[8]+"");
                            customerDetails.setCurrentStatus(object[9]+"");
                            CustomerDetailsList.add(customerDetails);
                        }
                    }
                    listData = CustomerDetailsList;
                }
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
