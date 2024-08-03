package com.enach.Controller;


import com.enach.Models.*;
import com.enach.Service.DatabaseService;
import com.enach.Service.ReqstrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/customer")
@CrossOrigin
public class ReqstrController {

    private final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    private ReqstrService reqstrService;
    @Autowired
    private DatabaseService databaseService;

    @GetMapping("/mandateType")
    public ResponseEntity<String> mandateType(@RequestParam("applicationNo") String applicationNo, @RequestParam("mandateType") String mandateType) {

        CommonResponse commonResponse = new CommonResponse();
        MandateTypeAmountResponse mandateTypeAmountResponse = new MandateTypeAmountResponse();

        try {
            if (StringUtils.isEmpty(applicationNo) || StringUtils.isEmpty(mandateType)) {
                commonResponse.setMsg("Required field is empty.");
                commonResponse.setCode("1111");
                return new ResponseEntity(commonResponse, HttpStatus.OK);
            }

            MandateTypeAmountData mandateTypeAmountData = databaseService.mandateTypeAmount(applicationNo);
            if ("MNTH".equalsIgnoreCase(mandateType)) {
                mandateTypeAmountResponse.setAmount(BigDecimal.valueOf(mandateTypeAmountData.getInstallmentAmount()*2));
                mandateTypeAmountResponse.setCode("0000");
                mandateTypeAmountResponse.setMsg("success e-mandate amount");

                logger.info("E-mandate amount for {} {}",applicationNo, mandateTypeAmountResponse.getAmount());
                return new ResponseEntity(mandateTypeAmountResponse, HttpStatus.OK);

            } else if ("ADHO".equalsIgnoreCase(mandateType)) {
                mandateTypeAmountResponse.setAmount(BigDecimal.valueOf(mandateTypeAmountData.getSanctionLoanAmount()));
                mandateTypeAmountResponse.setCode("0000");
                mandateTypeAmountResponse.setMsg("success security-mandate amount");

                logger.info("Security-mandate amount for {} {}",applicationNo, mandateTypeAmountResponse.getAmount());
                return new ResponseEntity(mandateTypeAmountResponse, HttpStatus.OK);

            } else {
                commonResponse.setMsg("Please try again.");
                commonResponse.setCode("1111");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            commonResponse.setMsg("Something went wrong.");
            commonResponse.setCode("1111");
            logger.error("Something went wrong while fetching mandate-type amount for {} {}",applicationNo, e.getMessage());
        }
        return new ResponseEntity(commonResponse, HttpStatus.OK);
    }


    @PostMapping("/enachPayment")
    public ResponseEntity<String> enachPayment(@RequestBody EnachPaymentRequest request) {

        CommonResponse commonResponse = new CommonResponse();
        logger.info("Save transaction details for {} ",request.getApplicationNo());

        try {

                if (StringUtils.isEmpty(request.getTransactionNo()) || StringUtils.isEmpty(request.getMandateType()) || StringUtils.isEmpty(request.getApplicationNo()) || StringUtils.isEmpty(request.getPaymentMethod()) || StringUtils.isEmpty(request.getTransactionStartDate())) {
                commonResponse.setMsg("Required field is empty.");
                commonResponse.setCode("1111");
                return new ResponseEntity(commonResponse, HttpStatus.OK);
            }

            reqstrService.saveEnachPayment(request);

            commonResponse.setMsg("eNachPayment save successfully.");
            commonResponse.setMsg("Response Save.");
            commonResponse.setCode("0000");
            return new ResponseEntity(commonResponse, HttpStatus.OK);

        } catch (DataIntegrityViolationException ex) {
            commonResponse.setMsg("Duplicate request.");
            commonResponse.setCode("1111");
            logger.warn("Transaction-No already exist {}",request.getTransactionNo());
        } catch (Exception e) {
            commonResponse.setMsg("something went wrong.");
            commonResponse.setCode("1111");
            logger.info("Something went wrong while saving transaction details for {} {}",request.getApplicationNo(),e.getMessage());

        }
        return new ResponseEntity(commonResponse, HttpStatus.OK);
    }

}
