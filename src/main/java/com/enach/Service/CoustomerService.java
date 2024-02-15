package com.enach.Service;

import com.enach.Entity.CustomerDetails;
import com.enach.Entity.ReqStrDetails;
import com.enach.Models.ReqStrDetailsResponse;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

public interface CoustomerService {
    HashMap<String, String> validateAndSendOtp(String loanNo);


    CustomerDetails getCustomerDetail(String mobileNo, String otpCode);

    ReqStrDetails getReqStrDetailsByCustomerAccountNo(String customerAccountNo);

}
