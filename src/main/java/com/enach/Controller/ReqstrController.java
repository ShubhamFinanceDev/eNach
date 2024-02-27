package com.enach.Controller;


import com.enach.Entity.MandateRespDoc;
import com.enach.Models.CommonResponse;
import com.enach.Models.MandateRespDocResponse;
import com.enach.Service.ReqstrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> mandateRespDoc(@RequestBody MandateRespDocResponse request) {

        CommonResponse commonResponse = new CommonResponse();


            try {

                MandateRespDoc mandateRespDoc = reqstrService.saveMandateRespDoc(request.getStatus(),request.getMsgId(),request.getRefId(),request.getErrors(),request.getErrorCode(),request.getErrorMessage(),request.getFiller1(),request.getFiller2(),request.getFiller3(),request.getFiller4(),request.getFiller5(),request.getFiller6(),request.getFiller7(),request.getFiller8(),request.getFiller9(),request.getFiller10());
                commonResponse.setMsg("Response Save.");
                commonResponse.setCode("0000");

            } catch (Exception e) {
                commonResponse.setMsg("something went worng.");
                commonResponse.setCode("1111");
            }

        return new ResponseEntity(commonResponse, HttpStatus.OK);
    }






}
