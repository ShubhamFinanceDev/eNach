package com.enach.Controller;

import com.enach.Entity.CustomerDetails;
import com.enach.Models.CommonResponse;
import com.enach.Models.OtpRequest;
import com.enach.Models.OtpVerifyResponse;
import com.enach.Models.OtpVerifyResponseForCancellation;
import com.enach.Service.CancellationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/enach-cancel")
public class CancellationController {


    @Autowired
    private CancellationService cancellationService;

    @PostMapping("/otpVerification")
    public ResponseEntity<OtpVerifyResponseForCancellation> login(@RequestBody OtpRequest request) {
        OtpVerifyResponseForCancellation otpVerifyResponse = new OtpVerifyResponseForCancellation();

        if (request.getMobileNo().isBlank() || request.getOtpCode().isBlank() || request.getApplicationNo().isBlank()) {
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setMsg("Field is empty");
            commonResponse.setCode("0000");
            return new ResponseEntity(commonResponse, HttpStatus.OK);
        } else {
            try {
                // Fetch customer details
                List<CustomerDetails> customerDetailsList = cancellationService.getCustomerDetail(
                        request.getMobileNo(), request.getOtpCode(), request.getApplicationNo()
                );

                // Check and log the customerDetailsList
                System.out.println("This is list data: " + customerDetailsList);
                if (customerDetailsList != null && !customerDetailsList.isEmpty()) {
                    CustomerDetails customerDetails = customerDetailsList.get(0);

                    otpVerifyResponse.setData1(new OtpVerifyResponseForCancellation.Data1());
                    otpVerifyResponse.getData1().setApplicationNo(customerDetails.getApplicationNumber());
                    otpVerifyResponse.getData1().setCustomerName(customerDetails.getCustomerName());
                    otpVerifyResponse.getData1().setMobileNo(request.getMobileNo());

                    List<OtpVerifyResponseForCancellation.LoanDetails> loanDetailsList = new ArrayList<>();
                    for (CustomerDetails details : customerDetailsList) {
                        OtpVerifyResponseForCancellation.LoanDetails loanDetail = new OtpVerifyResponseForCancellation.LoanDetails();
                        loanDetail.setLoanNo(details.getLoanAccountNo());
                        loanDetail.setStatus(details.getCurrentStatus());
                        loanDetailsList.add(loanDetail);
                    }
                    otpVerifyResponse.setLoanDetails(loanDetailsList);

                    return new ResponseEntity(otpVerifyResponse, HttpStatus.OK);
                } else {
                    CommonResponse commonResponse = new CommonResponse();
                    commonResponse.setMsg("Otp is invalid or expired, please try again.");
                    commonResponse.setCode("1111");
                    return new ResponseEntity(commonResponse, HttpStatus.OK);
                }
            } catch (Exception e) {
                CommonResponse commonResponse = new CommonResponse();
                commonResponse.setMsg("Phone number does not exist.");
                commonResponse.setCode("1111");
                e.printStackTrace(); // Log the stack trace for debugging
                return new ResponseEntity(commonResponse, HttpStatus.OK);
            }
        }
    }
}
