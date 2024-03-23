package com.enach.Service;

import com.enach.Entity.EnachPayment;
import com.enach.Entity.ResponseStructure;
import com.enach.Models.EnachPaymentStatusRequest;
import com.enach.Models.MandateTypeAmountResponse;

import java.sql.Timestamp;
import java.time.LocalDateTime;


public interface ReqstrService {


    MandateTypeAmountResponse getMandateTypeAmount(String loanNo);

    EnachPayment saveEnachPayment(String transactionNo, String loanNo, Timestamp transactionStartDate) throws Exception;

    EnachPayment updateEnachPaymentStatus(String transactionNo, String transactionStatus);

}

