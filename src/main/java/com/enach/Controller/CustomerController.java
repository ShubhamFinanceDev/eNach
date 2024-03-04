package com.enach.Controller;


import com.enach.Models.CommonResponse;
import com.enach.Models.CustomerDetails;
import com.enach.Models.OtpRequest;
import com.enach.Models.OtpVerifyResponse;
import com.enach.Service.CoustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
private CoustomerService coustomerService;


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
        if (request.getMobileNo().isBlank() || request.getOtpCode().isBlank()) {
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setMsg("Required field is empty.");
            commonResponse.setCode("1111");
            return new ResponseEntity(commonResponse, HttpStatus.OK);

        } else {

            try {

                CustomerDetails customerDetails = coustomerService.getCustomerDetail(request.getMobileNo(), request.getOtpCode());

                if (customerDetails != null) {

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
                    otpVerifyResponse.setAmount(customerDetails.getAmount());
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
                return new ResponseEntity(commonResponse, HttpStatus.OK);
            }

        }
    }

}
