package com.enach.Service;

import com.enach.Entity.EnachPayment;
import com.enach.Models.MandateTypeAmountResponse;

import java.sql.Timestamp;


public interface ReqstrService {


    MandateTypeAmountResponse getMandateTypeAmount(String loanNo);

    EnachPayment saveEnachPayment(String transactionNo, String loanNo, String mandateType, Timestamp transactionStartDate) throws Exception;

    EnachPayment updateEnachPaymentStatus(String transactionNo, String transactionStatus,String errorMessage);

    void sendEmailOnBank(String emailId, String loanNo, String transactionNo, String transactionStatus, String errorMessage);

}

