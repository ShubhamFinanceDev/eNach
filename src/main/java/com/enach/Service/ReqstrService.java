package com.enach.Service;

import com.enach.Entity.EnachPayment;
import com.enach.Models.MandateTypeAmountData;

import java.math.BigDecimal;
import java.sql.Timestamp;


public interface ReqstrService {


    void saveEnachPayment(String transactionNo, String applicationNo, String paymentMethod, String mandateType, Timestamp transactionStartDate, BigDecimal amount) throws Exception;

}

