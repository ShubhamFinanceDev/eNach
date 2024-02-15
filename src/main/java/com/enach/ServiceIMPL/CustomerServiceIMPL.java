package com.enach.ServiceIMPL;


import com.enach.Entity.CustomerDetails;
import com.enach.Entity.OtpDetails;
import com.enach.Entity.ReqStrDetails;
import com.enach.Models.ReqStrDetailsResponse;
import com.enach.Repository.CustomerDetailsRepository;
import com.enach.Repository.OtpDetailsRepository;
import com.enach.Repository.ReqStrDetailsRepository;
import com.enach.Service.CoustomerService;
import com.enach.Utill.EncryptionAES256;
import com.enach.Utill.OtpUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.enach.Utill.SHA256.getSHA;
import static com.enach.Utill.SHA256.toHexString;


@Service
public class CustomerServiceIMPL implements CoustomerService {

    @Autowired
    private CustomerDetailsRepository customerDetailsRepository;
    @Autowired
    private OtpUtility otpUtility;
    @Autowired
    private OtpDetailsRepository otpDetailsRepository;
    @Autowired
    private ReqStrDetailsRepository reqStrDetailsRepository;

    Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

    public HashMap validateAndSendOtp(String loanNo) {
        HashMap<String, String> otpResponse = new HashMap<>();
        CustomerDetails customerDetails = new CustomerDetails();

        try {


          customerDetails = customerDetailsRepository.findByLoanNo(loanNo);

            if (customerDetails != null) {
                int otpCode = otpUtility.generateOtp(customerDetails);

                if (otpCode > 0) {
                    logger.info("otp generated successfully");
               //    if (otpUtility.sendOtp(customerDetails.getMobileNo(), otpCode)) {
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
            //        otpResponse.put("msg", "Otp did not send, please try again");
            //        otpResponse.put("code", "1111");
            //    }

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
                Duration duration = Duration.between(otpDetails.getOtpExprTime(), LocalDateTime.now());
                customerDetails = (duration.toMinutes() > 5) ? null : customerDetailsRepository.findCustomerDetailByMobile(mobileNo);
            } else {
                customerDetails = null;
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return customerDetails;
    }

    @Override
    public ReqStrDetails getReqStrDetailsByCustomerAccountNo(String customerAccountNo) {

        ReqStrDetails reqStrDetailsData = new ReqStrDetails();
        reqStrDetailsData = reqStrDetailsRepository.findByReqStrDetailsByCustomerAccountNo(customerAccountNo);
        return reqStrDetailsData;
    }


}
