package com.enach.Service;

import com.enach.Entity.EnachPayment;
import com.enach.Models.EnachPaymentRequest;
import com.enach.Models.MandateTypeAmountData;

import java.math.BigDecimal;
import java.sql.Timestamp;


public interface ReqstrService {


    void saveEnachPayment(EnachPaymentRequest request) throws Exception;

}

