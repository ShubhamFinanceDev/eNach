package com.enach.Service;

import com.enach.Entity.EnachPayment;
import com.enach.Entity.ResponseStructure;
import com.enach.Models.EnachPaymentStatusRequest;

import java.sql.Timestamp;
import java.time.LocalDateTime;


public interface ReqstrService {


    ResponseStructure saveMandateRespDoc(String checkSumVal, String status, String msgId,  String refId, String errorCode, String errorMessage, String filler1, String filler2, String filler3, String filler4, String filler5, String filler6, String filler7, String filler8, String filler9, String filler10);

    EnachPayment saveEnachPayment(String transactionNo, String loanNo, Timestamp transactionStartDate);

    EnachPayment updateEnachPaymentStatus(String transactionNo, String transactionStatus);

}

