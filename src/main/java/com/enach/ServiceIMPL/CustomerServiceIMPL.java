package com.enach.ServiceIMPL;


import com.enach.Entity.OtpDetails;
import com.enach.Models.CustomerDetails;
import com.enach.Models.MandateTypeAmountResponse;
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
import org.springframework.web.filter.OncePerRequestFilter;

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

    Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);


    @Override
    public HashMap<String, String> validateCustAndSendOtp(String loanNo) {

        HashMap<String, String> otpResponse = new HashMap<>();

        String sql = "SELECT * FROM customer_details WHERE loan_no='"+loanNo+"';";
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

                String sql = "SELECT * FROM customer_details WHERE mobile_no='"+mobileNo+"';";
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


/*
    @Override
    public EMandateAmountResponse getEMandateAmount(String loanNo) {

        EMandateAmountResponse eMandateAmountResponse = new EMandateAmountResponse();

        try {

            String sql = "SELECT amount FROM customer_details WHERE loan_no='"+loanNo+"';";
            List<EMandateAmountResponse> listData = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(EMandateAmountResponse.class));

            if(!listData.isEmpty() && listData.size()>0) {
                eMandateAmountResponse = listData.get(0);
            }else{
                eMandateAmountResponse = null;

            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return eMandateAmountResponse;
    }

    @Override
    public SecurityMandateAmountResponse getSecutityMandateAmount(String loanNo) {

        SecurityMandateAmountResponse secutityMandateAmountResponse = new SecurityMandateAmountResponse();
        try {

            String sql = "SELECT secutity_amount FROM customer_details WHERE loan_no='"+loanNo+"';";
            List<SecurityMandateAmountResponse> listData = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(SecurityMandateAmountResponse.class));

            if(!listData.isEmpty() && listData.size()>0) {
                secutityMandateAmountResponse = listData.get(0);
            }else{

                secutityMandateAmountResponse = null;

            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return secutityMandateAmountResponse;
    }

    */




    @Override
    public MandateTypeAmountResponse getMandateTypeAmount(String loanNo) {

        MandateTypeAmountResponse mandateTypeAmountResponse = new MandateTypeAmountResponse();

        try {

            String sql = "SELECT amount FROM customer_details WHERE loan_no='"+loanNo+"';";
            List<MandateTypeAmountResponse> listData = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(MandateTypeAmountResponse.class));

            if(!listData.isEmpty() && listData.size()>0) {
                mandateTypeAmountResponse = listData.get(0);
            }else{
                mandateTypeAmountResponse = null;

            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return mandateTypeAmountResponse;
    }


}
