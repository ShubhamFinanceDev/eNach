package com.enach.Service;



import com.enach.Models.CustomerDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

public interface CoustomerService {

    HashMap<String, String> validateCustAndSendOtp(String loanNo);

    CustomerDetails getCustomerDetail(String mobileNo, String otpCode);

}
