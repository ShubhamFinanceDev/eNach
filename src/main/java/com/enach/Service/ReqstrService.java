package com.enach.Service;

import com.enach.Entity.EnachPayment;
import com.enach.Models.MandateTypeAmountResponse;
import com.enach.Models.SecurityMandateTypeAmountResponse;

import java.sql.Timestamp;


public interface ReqstrService {


    MandateTypeAmountResponse getMandateTypeAmount(String applicationNo);

    SecurityMandateTypeAmountResponse getsecurityMandateTypeAmount(String applicationNo);

    EnachPayment saveEnachPayment(String transactionNo, String applicationNo, String mandateType, Timestamp transactionStartDate) throws Exception;



}

