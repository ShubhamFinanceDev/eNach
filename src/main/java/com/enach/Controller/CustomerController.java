package com.enach.Controller;


import com.enach.Entity.CustomerDetails;
import com.enach.Entity.ReqStrDetails;
import com.enach.Models.*;
import com.enach.Service.CoustomerService;
import com.enach.Utill.EncryptionAES256;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.enach.Utill.SHA256.getSHA;
import static com.enach.Utill.SHA256.toHexString;

@RestController
@RequestMapping("/eNach")
@CrossOrigin
public class CustomerController {

@Autowired
private CoustomerService coustomerService;

    @PostMapping("/sendOtp")
    public HashMap sendOtpOnRegisteredMobile(@RequestBody Map<String, String> inputParam) {
        String loanNo = inputParam.get("loanNo");
        HashMap<String, String> otpResponse = new HashMap<>();

        System.out.println("===LOAN NO====="+loanNo);
        if (loanNo.isEmpty()) {
            otpResponse.put("msg", "Loan number field is empty");
            otpResponse.put("code", "1111");
        } else {
            otpResponse = coustomerService.validateAndSendOtp(loanNo);
        }

        return otpResponse;
    }


    @PostMapping("/otpVerification")
    public ResponseEntity<OtpVerifyResponse> login(@RequestBody OtpRequest request) {

        System.out.println("========"+request);
        OtpVerifyResponse otpVerifyResponse = new OtpVerifyResponse();
        if(request.getMobileNo().isBlank() || request.getOtpCode().isBlank()) {
            otpVerifyResponse.setMsg("Required field is empty.");
            otpVerifyResponse.setCode("1111");

        }else{

            try {

                CustomerDetails customerDetails = coustomerService.getCustomerDetail(request.getMobileNo(), request.getOtpCode());
                if (customerDetails != null) {

                    otpVerifyResponse.setCustName(customerDetails.getCustName());
                    otpVerifyResponse.setLoanNo(customerDetails.getLoanNo());
                    otpVerifyResponse.setMobileNo(customerDetails.getMobileNo());
                    otpVerifyResponse.setEmail(customerDetails.getEmail());
                    otpVerifyResponse.setStartDate(customerDetails.getStartDate());
                    otpVerifyResponse.setExpiryDate(customerDetails.getExpiryDate());
                    otpVerifyResponse.setAmount(customerDetails.getAmount());


                } else {
                    otpVerifyResponse.setMsg("Otp is invalid or expired, please try again.");
                    otpVerifyResponse.setCode("1111");
                }
            }
            catch (Exception e)
            {
                otpVerifyResponse.setMsg("phone number does not exist.");
                otpVerifyResponse.setCode("1111");
            }

        }

        return new ResponseEntity<>(otpVerifyResponse, HttpStatus.OK);
    }


    @CrossOrigin
    @GetMapping("/reqStrDetailsData")
    public ResponseEntity<ReqStrDetailsResponse>  reqStrDetailsData(@RequestParam(name = "customerAccountNo", required = false) String customerAccountNo)  {
      ReqStrDetailsResponse reqStrDetailsResponse = new ReqStrDetailsResponse();
      ReqStrDetails reqStrDetailsData = new ReqStrDetails();

      if (customerAccountNo != null) {
          reqStrDetailsData = coustomerService.getReqStrDetailsByCustomerAccountNo(customerAccountNo);
      }else {
          reqStrDetailsResponse.setCode("1111");
          reqStrDetailsResponse.setMsg("Please select required field");
      }

      if (reqStrDetailsResponse.getCode() == null) {
          if (reqStrDetailsData==null) {
              reqStrDetailsResponse.setCode("1111");
              reqStrDetailsResponse.setMsg("Data not found");
          }else{

              try
              {

                  String secretKey = "k2hLr4X0ozNyZByj5DT66edtCEee1x+6";
                  String salt = "MySalt";

                  String custName=reqStrDetailsData.getCustomerName();
                  String enccustName = EncryptionAES256.encrypt(custName, secretKey,salt);
                  String custMobileNo=reqStrDetailsData.getCustomerMobileNo();
                  String enccustMobileNo =  EncryptionAES256.encrypt(custMobileNo, secretKey,salt);
                  String custEmail = reqStrDetailsData.getCustomerEmailId();
                  String enccustEmail =  EncryptionAES256.encrypt(custEmail, secretKey,salt);
                  String custAcNo=reqStrDetailsData.getCustomerAccountNo();
                  String enccustAcNo = EncryptionAES256.encrypt(custAcNo, secretKey,salt);
                  String custStartDt= reqStrDetailsData.getCustomerStartDate();
                  String custExpDt=reqStrDetailsData.getCustomerExpiryDate();
                  Double custDebitAmount=reqStrDetailsData.getCustomerDebitAmount();
                  Double custMaxAmount=reqStrDetailsData.getCustomerMaxAmount();
                  String actualCheckSum = custAcNo+"|"+custStartDt+"|"+custExpDt+"|"+custDebitAmount+"|"+custMaxAmount;
                  String convertCheckSumSHA = toHexString(getSHA(actualCheckSum));
                  String shortCode = reqStrDetailsData.getShortCode();
                  String encshortCode = EncryptionAES256.encrypt(shortCode, secretKey,salt);
                  String custRefe1 = reqStrDetailsData.getCustomerReference1();
                  String enccustRefe1 = EncryptionAES256.encrypt(custRefe1, secretKey,salt);
                  String custRefe2 = reqStrDetailsData.getCustomerReference2();
                  String enccustRefe2 = EncryptionAES256.encrypt(custRefe2, secretKey,salt);
                  String utilCode = reqStrDetailsData.getCustomerReference2();
                  String encutilCode = EncryptionAES256.encrypt(utilCode, secretKey,salt);

                  reqStrDetailsResponse.setCheckSum(convertCheckSumSHA);
                  reqStrDetailsResponse.setMsgId(reqStrDetailsData.getMsgId());
                  reqStrDetailsResponse.setCustomerName(enccustName);
                  reqStrDetailsResponse.setCustomerMobileNo(enccustMobileNo);
                  reqStrDetailsResponse.setCustomerEmailId(enccustEmail);
                  reqStrDetailsResponse.setCustomerAccountNo(enccustAcNo);
                  reqStrDetailsResponse.setCustomerStartDate(custStartDt);
                  reqStrDetailsResponse.setCustomerExpiryDate(custExpDt);
                  reqStrDetailsResponse.setCustomerDebitAmount(custDebitAmount);
                  reqStrDetailsResponse.setCustomerMaxAmount(custMaxAmount);
                  reqStrDetailsResponse.setCustomerDebitFrequency(reqStrDetailsData.getCustomerDebitFrequency());
                  reqStrDetailsResponse.setCustomerInstructedMemberId(reqStrDetailsData.getCustomerInstructedMemberId());
                  reqStrDetailsResponse.setShortCode(encshortCode);
                  reqStrDetailsResponse.setCustomerSequenceType(reqStrDetailsData.getCustomerSequenceType());
                  reqStrDetailsResponse.setMerchantCategoryCode(reqStrDetailsData.getMerchantCategoryCode());
                  reqStrDetailsResponse.setCustomerReference1(enccustRefe1);
                  reqStrDetailsResponse.setCustomerReference2(enccustRefe2);
                  reqStrDetailsResponse.setChannel(reqStrDetailsData.getChannel());
                  reqStrDetailsResponse.setUtilCode(encutilCode);
                  reqStrDetailsResponse.setFiller1(reqStrDetailsData.getFiller1());
                  reqStrDetailsResponse.setFiller2(reqStrDetailsData.getFiller2());
                  reqStrDetailsResponse.setFiller3(reqStrDetailsData.getFiller3());
                  reqStrDetailsResponse.setFiller4(reqStrDetailsData.getFiller4());
                  reqStrDetailsResponse.setFiller5(reqStrDetailsData.getFiller5());
                  reqStrDetailsResponse.setFiller6(reqStrDetailsData.getFiller6());
                  reqStrDetailsResponse.setFiller7(reqStrDetailsData.getFiller7());
                  reqStrDetailsResponse.setFiller8(reqStrDetailsData.getFiller8());
                  reqStrDetailsResponse.setFiller9(reqStrDetailsData.getFiller9());
                  reqStrDetailsResponse.setFiller10(reqStrDetailsData.getFiller10());


              }
              catch (NoSuchAlgorithmException e) {
                  System.out.println("Exception thrown for incorrect algorithm: " + e);
              }
          }

      }

        return new ResponseEntity<ReqStrDetailsResponse>(reqStrDetailsResponse, HttpStatus.OK);

  }


    @PostMapping("/checkSum")
    public ResponseEntity<String> checkSum(@RequestBody OtpRequest request) {

        return null;
    }



}
