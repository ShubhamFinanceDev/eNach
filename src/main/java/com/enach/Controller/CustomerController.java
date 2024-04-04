package com.enach.Controller;


import com.enach.Entity.EnachPayment;
import com.enach.Models.*;
import com.enach.Service.CoustomerService;
import com.enach.sercurity.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
@Controller
@RestController
@RequestMapping("/eNach")
@CrossOrigin
public class CustomerController {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtHelper helper;

    @Autowired
    private CoustomerService coustomerService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String demo(){

        return "Hello programmer";
    }


    @PostMapping("/sendOtp")
    public HashMap sendOtpOnCustomerRegisteredMobile(@RequestBody Map<String, String> inputParam) {

        String loanNo = inputParam.get("loanNo");
        HashMap<String, String> otpResponse = new HashMap<>();

        if (loanNo.isEmpty()) {
            otpResponse.put("msg", "Loan number field is empty");
            otpResponse.put("code", "1111");
        } else {
            otpResponse = coustomerService.validateCustAndSendOtp(loanNo);
        }

        return otpResponse;
    }


    @PostMapping("/otpVerification")
    public ResponseEntity<OtpVerifyResponse> login(@RequestBody OtpRequest request) {

        OtpVerifyResponse otpVerifyResponse = new OtpVerifyResponse();
        if (request.getMobileNo().isBlank() || request.getOtpCode().isBlank() || request.getLoanNo().isBlank()) {
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setMsg("Required field is empty.");
            commonResponse.setCode("1111");
            return new ResponseEntity(commonResponse, HttpStatus.OK);

        } else {

            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(request.getLoanNo());
                String token = this.helper.generateToken(userDetails);

                CustomerDetails customerDetails = coustomerService.getCustomerDetail(request.getMobileNo(), request.getOtpCode());

                if (customerDetails != null) {

                    otpVerifyResponse.setJwtToken(token);
                    otpVerifyResponse.setCustName(customerDetails.getCustName());
                    otpVerifyResponse.setLoanNo(customerDetails.getLoanNo());
                    otpVerifyResponse.setMobileNo(customerDetails.getMobileNo());
                    otpVerifyResponse.setEmail(customerDetails.getEmail());

                    LocalDate startDate = customerDetails.getStartDate();
                    LocalDate today = LocalDate.now();
                    if(startDate.isBefore(today)){
                        otpVerifyResponse.setStartDate(today);
                    }else{
                        otpVerifyResponse.setStartDate(startDate);
                    }

                    LocalDate expiryDate = customerDetails.getExpiryDate();
                    LocalDate today1 = LocalDate.now();
                    if(expiryDate.isAfter(today1)){
                        otpVerifyResponse.setExpiryDate(expiryDate);
                    }
                    return new ResponseEntity(otpVerifyResponse, HttpStatus.OK);

                } else {
                    CommonResponse commonResponse = new CommonResponse();
                    commonResponse.setMsg("Otp is invalid or expired, please try again.");
                    commonResponse.setCode("1111");
                    return new ResponseEntity(commonResponse, HttpStatus.OK);
                }
            } catch (Exception e) {
                CommonResponse commonResponse = new CommonResponse();
                commonResponse.setMsg("phone number does not exist.");
                commonResponse.setCode("1111");
                System.out.println(e);
                return new ResponseEntity(commonResponse, HttpStatus.OK);
            }

        }
    }



    @PutMapping("/enachPaymentStatus/{transactionNo}")
    public ResponseEntity<String> enachPaymentStatus(@RequestBody EnachPaymentStatusRequest request , @PathVariable("transactionNo") String transactionNo) {

        StatusResponse statusResponse = new StatusResponse();
        CommonResponse commonResponse = new CommonResponse();

        try {
            if (StringUtils.isEmpty(transactionNo) || StringUtils.isEmpty(request.getTransactionStatus())) {
                commonResponse.setMsg("Required field is empty.");
                commonResponse.setCode("1111");
                return new ResponseEntity(commonResponse, HttpStatus.OK);
            }

            EnachPayment enachPayment = coustomerService.updateEnachPaymentStatus(transactionNo,request.getTransactionStatus(),request.getErrorMessage());

            if (enachPayment != null && !StringUtils.isEmpty(enachPayment)){

                // String emailId = "nainish.singh@dbalounge.com";
                String emailId = "abhialok5499@gmail.com";

                coustomerService.sendEmailOnBank(emailId, transactionNo,request.getTransactionStatus(),request.getErrorMessage());

                statusResponse.setLoanNo(enachPayment.getLoanNo());
                statusResponse.setMsg("update paymentstatus.");
                statusResponse.setCode("0000");
                return new ResponseEntity(statusResponse, HttpStatus.OK);

            }else{
                commonResponse.setMsg("transactionno does not exist.");
                commonResponse.setCode("1111");
            }

        } catch (Exception e) {
            commonResponse.setMsg("something went worng.");
            commonResponse.setCode("1111");
        }
        return new ResponseEntity(commonResponse, HttpStatus.OK);
    }

}
