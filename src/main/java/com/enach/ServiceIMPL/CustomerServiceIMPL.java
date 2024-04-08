package com.enach.ServiceIMPL;


import com.enach.Entity.EnachPayment;
import com.enach.Entity.OtpDetails;
import com.enach.Models.CustomerDetails;
import com.enach.Models.EmailDetails;
import com.enach.Repository.EnachPaymentRepository;
import com.enach.Repository.OtpDetailsRepository;
import com.enach.Service.CoustomerService;
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

        String sql = "SELECT * FROM enach WHERE Application_Number='"+applicationNo+"';";
        try {
            List<CustomerDetails>  listData = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(CustomerDetails.class));

            if(!listData.isEmpty() && listData.size()>0) {
                CustomerDetails customerDetails = listData.get(0);

                int otpCode = otpUtility.generateCustOtp(customerDetails);

                if(otpCode >0){

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

            } else {
                otpResponse.put("msg", "Loan no not found");
                otpResponse.put("code", "1111");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return otpResponse;
    }



    @Override
    public CustomerDetails getCustomerDetail(String mobileNo, String otpCode) {

        CustomerDetails customerDetails = new CustomerDetails();
        try {
            OtpDetails otpDetails = otpDetailsRepository.IsotpExpired(mobileNo, otpCode);
            if (otpDetails != null) {

                String sql = "SELECT * FROM enach WHERE mobile_no='"+mobileNo+"';";
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
    public void sendEmailOnBank(String emailId, String transactionNo, String transactionStatus,String errorMessage) {

        String mandateType = "";
        String applicationNo = "";

        List<?> dataList = enachPaymentRepository.findLoanNoAndMandateType(transactionNo);

        if(!dataList.isEmpty()) {
            Object[] obj = (Object[]) dataList.get(0);
            mandateType = ""+obj[0];
            applicationNo = ""+obj[1];
        }


        EmailDetails emailDetails = new EmailDetails();
        try {
            if("Sucuss".equalsIgnoreCase(transactionStatus)) {
                emailDetails.setRecipient(emailId);
                emailDetails.setSubject("E-NACH SHUBHAM");
                emailDetails.setMsgBody(""+mandateType+" has been sucussfully E-Nach.\n" +
                        "for ApplicationNo "+applicationNo+" and transactionNo "+transactionNo+"\n"+
                        "Regards\n" +
                        "Shubham Housing Development Finance Company");

                otpUtility.sendSimpleMail(emailDetails);

            }else if ("Failed".equalsIgnoreCase(transactionStatus)){
                emailDetails.setRecipient(emailId);
                emailDetails.setSubject("E-NACH SHUBHAM");
                emailDetails.setMsgBody(""+mandateType+" has been failed E-Nach.\n" +
                        "for ApplicationNo"+applicationNo+" and transactionNo"+transactionNo+" Due to "+errorMessage+".\n"+
                        "Regards\n" +
                        "Shubham Housing Development Finance Company");

                otpUtility.sendSimpleMail(emailDetails);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }


}
