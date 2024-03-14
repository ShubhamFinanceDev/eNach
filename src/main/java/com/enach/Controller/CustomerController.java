package com.enach.Controller;


import com.enach.Models.*;
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


    @GetMapping("/mandateType")
    public ResponseEntity<String> mandateType(@RequestParam("loanNo") String loanNo, @RequestParam("mandateType") String  mandateType) {

        CommonResponse commonResponse = new CommonResponse();
        MandateTypeAmountResponse mandateTypeAmountResponse = new MandateTypeAmountResponse();

        try{

            MandateTypeAmountResponse mandateTypeAmount = coustomerService.getMandateTypeAmount(loanNo);

            if("MNTH".equalsIgnoreCase(mandateType)){

                mandateTypeAmountResponse.setAmount(mandateTypeAmount.getAmount());
                mandateTypeAmountResponse.setCode("0000");
                mandateTypeAmountResponse.setMsg("succuss emandate amount");

                return new ResponseEntity(mandateTypeAmountResponse, HttpStatus.OK);

            }else if ("ADHO".equalsIgnoreCase(mandateType)){

                Double emiAmount = mandateTypeAmount.getAmount();
                mandateTypeAmountResponse.setAmount(emiAmount/2);
                mandateTypeAmountResponse.setCode("0000");
                mandateTypeAmountResponse.setMsg("succuss emandate amount");

                return new ResponseEntity(mandateTypeAmountResponse, HttpStatus.OK);

            }else {

                commonResponse.setMsg("mandateType is not correct.");
                commonResponse.setCode("1111");
            }

        }
        catch (Exception ex) {
            commonResponse.setMsg("Please try again.");
            commonResponse.setCode("1111");
        }
        return new ResponseEntity(commonResponse, HttpStatus.OK);
    }



}
