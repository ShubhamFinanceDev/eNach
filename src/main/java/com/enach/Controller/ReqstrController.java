package com.enach.Controller;


import com.enach.Entity.EnachPayment;
import com.enach.Entity.ResponseStructure;
import com.enach.Models.*;
import com.enach.Service.ReqstrService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/eNach")
@CrossOrigin
public class ReqstrController {

    @Autowired
    private ReqstrService reqstrService;


    @GetMapping("/msgId")
    public ResponseEntity<String> msgId() {
        CommonResponse commonResponse = new CommonResponse();
        String finalMsgId = "";
        String msg1 = "SUBH";
        try{
            SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String dateString=sdf.format(new Date());
            finalMsgId = msg1+dateString;
            commonResponse.setMsg(finalMsgId);
            commonResponse.setCode("0000");
        }
        catch (Exception ex) {
            commonResponse.setMsg("Please try again.");
            commonResponse.setCode("1111");
        }
        return new ResponseEntity(commonResponse, HttpStatus.OK);
    }



    @PostMapping("/mandateRespDoc")
    public ResponseEntity<String> mandateRespDoc(@RequestBody ResponseStructureRequest request) {

        CommonResponse commonResponse = new CommonResponse();

            try {

                MandateRespDoc mandateRespDoc = request.getMandateRespDoc();

                ResponseStructure responseStructure = reqstrService.saveMandateRespDoc(request.getCheckSumVal(),mandateRespDoc.getStatus(),mandateRespDoc.getMsgId(),mandateRespDoc.getRefId(),mandateRespDoc.getErrorCode(),mandateRespDoc.getErrorMessage(),mandateRespDoc.getFiller1(),mandateRespDoc.getFiller2(),mandateRespDoc.getFiller3(),mandateRespDoc.getFiller4(),mandateRespDoc.getFiller5(),mandateRespDoc.getFiller6(),mandateRespDoc.getFiller7(),mandateRespDoc.getFiller8(),mandateRespDoc.getFiller9(),mandateRespDoc.getFiller10());
                commonResponse.setMsg("Response Save.");
                commonResponse.setCode("0000");

            } catch (Exception e) {
                commonResponse.setMsg("something went worng.");
                commonResponse.setCode("1111");
            }

        return new ResponseEntity(commonResponse, HttpStatus.OK);
    }



    @PostMapping("/enachPayment")
    public ResponseEntity<String> enachPayment(@RequestBody EnachPaymentRequest request ) {

        CommonResponse commonResponse = new CommonResponse();

        try {

            EnachPayment enachPayment = reqstrService.saveEnachPayment(request.getTransactionNo(), request.getLoanNo(), request.getTransactionStartDate());

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

            EnachPayment enachPayment = reqstrService.updateEnachPaymentStatus(transactionNo,request.getTransactionStatus());

            if (enachPayment != null && !StringUtils.isEmpty(enachPayment)){


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
