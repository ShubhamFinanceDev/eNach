package com.enach.Service;



import com.enach.Models.CustomerDetails;
import com.enach.Models.MandateTypeAmountResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

public interface CoustomerService {

    HashMap<String, String> validateCustAndSendOtp(String loanNo);

    CustomerDetails getCustomerDetail(String mobileNo, String otpCode);

    MandateTypeAmountResponse getMandateTypeAmount(String loanNo);
}
