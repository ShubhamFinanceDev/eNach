package com.enach.Service;



import com.enach.Entity.EnachPayment;
import com.enach.Entity.CustomerDetails;
import com.enach.Models.CommonResponse;

import java.util.HashMap;

public interface CoustomerService {

    HashMap<String, String> validateCustAndSendOtp(String applicationNo);

    CustomerDetails getCustomerDetail(String mobileNo, String otpCode, String applicationNo);

    EnachPayment updateEnachPaymentStatus(String transactionNo, String transactionStatus, String errorMessage, String refrenceId, String umrn);

    void sendEmailOnBank(String transactionNo, String transactionStatus, String errorMessage);

    CommonResponse generateReportOnMail();
}
