package com.enach.Service;



import com.enach.Entity.EnachPayment;
import com.enach.Models.CustomerDetails;
import com.enach.Models.MandateTypeAmountResponse;

import java.util.HashMap;

public interface CoustomerService {

    HashMap<String, String> validateCustAndSendOtp(String loanNo);

    CustomerDetails getCustomerDetail(String mobileNo, String otpCode);

    EnachPayment updateEnachPaymentStatus(String transactionNo, String transactionStatus, String errorMessage);

    void sendEmailOnBank(String emailId, String transactionNo, String transactionStatus, String errorMessage);


}
