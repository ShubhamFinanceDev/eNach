package com.enach.Controller;


import com.enach.Entity.EnachPayment;
import com.enach.Models.*;
import com.enach.Service.ReqstrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@CrossOrigin
public class ReqstrController {

    @Autowired
    private ReqstrService reqstrService;

    @GetMapping("/mandateType")
    public ResponseEntity<String> mandateType(@RequestParam("loanNo") String loanNo, @RequestParam("mandateType") String  mandateType) {

        CommonResponse commonResponse = new CommonResponse();
        MandateTypeAmountResponse mandateTypeAmountResponse = new MandateTypeAmountResponse();

        try{

            MandateTypeAmountResponse mandateTypeAmount = reqstrService.getMandateTypeAmount(loanNo);

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


    @PostMapping("/enachPayment")
    public ResponseEntity<String> enachPayment(@RequestBody EnachPaymentRequest request ) {

        CommonResponse commonResponse = new CommonResponse();

        try {
            String mandateType = ("MNTH".equalsIgnoreCase(request.getMandateType())) ? "e-Mandate" : "security-mandate";

            EnachPayment enachPayment = reqstrService.saveEnachPayment(request.getTransactionNo(), request.getLoanNo(), mandateType,request.getTransactionStartDate());

            commonResponse.setMsg("Response Save.");
            commonResponse.setCode("0000");

        } catch (DataIntegrityViolationException ex) {

            commonResponse.setMsg("Dublicate request.");
            commonResponse.setCode("1111");

        } catch (Exception e) {
            commonResponse.setMsg("something went worng.");
            commonResponse.setCode("1111");
        }
        return new ResponseEntity(commonResponse, HttpStatus.OK);
    }



    @PutMapping("/enachPaymentStatus/{transactionNo}")
    public ResponseEntity<String> enachPaymentStatus(@RequestBody EnachPaymentStatusRequest request , @PathVariable("transactionNo") String transactionNo) {

        StatusResponse statusResponse = new StatusResponse();
        CommonResponse commonResponse = new CommonResponse();

        try {

            EnachPayment enachPayment = reqstrService.updateEnachPaymentStatus(transactionNo,request.getTransactionStatus(),request.getErrorMessage());

            if (enachPayment != null && !StringUtils.isEmpty(enachPayment)){


                String loanNo = request.getLoanNo();
               // String emailId = "nainish.singh@dbalounge.com";
                String emailId = "abhialok5499@gmail.com";

                reqstrService.sendEmailOnBank(emailId, loanNo, transactionNo,request.getTransactionStatus(),request.getErrorMessage());

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
