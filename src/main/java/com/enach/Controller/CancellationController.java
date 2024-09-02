package com.enach.Controller;

import com.enach.Entity.CustomerDetails;
import com.enach.Models.*;
import com.enach.Service.CancellationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/cancellation")
public class CancellationController {


    @Autowired
    private CancellationService cancellationService;

    @PostMapping("/otpVerification")
    public ResponseEntity<OtpVerifyResponseForCancellation> login(@RequestBody OtpRequest request) {
        OtpVerifyResponseForCancellation otpVerifyResponse = new OtpVerifyResponseForCancellation();

        if (request.getMobileNo().isBlank() || request.getOtpCode().isBlank() || request.getApplicationNo().isBlank()) {
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setMsg("Field is empty");
            commonResponse.setCode("1111");
            return new ResponseEntity(commonResponse, HttpStatus.OK);
        } else {
            try {
                // Fetch customer details
                List<CustomerDetails> customerDetailsList = cancellationService.getCustomerDetail(
                        request.getMobileNo(), request.getOtpCode(), request.getApplicationNo()
                );

                if (customerDetailsList != null && !customerDetailsList.isEmpty()) {
                    CustomerDetails customerDetails = customerDetailsList.get(0);

                    otpVerifyResponse.setData(new OtpVerifyResponseForCancellation.Data());
                    otpVerifyResponse.getData().setApplicationNo(customerDetails.getApplicationNumber());
                    otpVerifyResponse.getData().setCustomerName(customerDetails.getCustomerName());
                    otpVerifyResponse.getData().setMobileNo(request.getMobileNo());

                    List<NestedLoansDetails> nestedLoansDetailsList =new ArrayList<>();

                    for (CustomerDetails details : customerDetailsList) {
                        NestedLoansDetails nestedLoansDetails=new NestedLoansDetails();
                        nestedLoansDetails.setLoanNo(details.getLoanAccountNo());
                        nestedLoansDetails.setStatus(details.getCurrentStatus());
                        nestedLoansDetailsList.add(nestedLoansDetails);
                    }
                    otpVerifyResponse.setLoansDetails(nestedLoansDetailsList);
                    otpVerifyResponse.setCode("0000");
                    otpVerifyResponse.setMsg("Otp verified successfully");

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

    @PostMapping("/cancellation-status")
    public ResponseEntity<?> saveStatus(@RequestBody SaveStatusRequest statusRequest){
        CommonResponse commonResponse=new CommonResponse();
        try {
            statusRequest.validate();
            commonResponse.setMsg(cancellationService.statusRequest(statusRequest));
            return ResponseEntity.ok(commonResponse);
        } catch (IllegalArgumentException e) {
            commonResponse.setMsg(e.getMessage());
            return ResponseEntity.badRequest().body(commonResponse);
        }
        catch (Exception e) {
            commonResponse.setMsg(e.getMessage());
            return ResponseEntity.internalServerError().body(commonResponse);
        }
    }
}
