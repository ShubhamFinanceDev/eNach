package com.enach.Utill;


import com.enach.Repository.OtpDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.HashMap;

@Service
public class OtpUtility {

    @Value("${otp.url}")
    private String otpUrl;
    @Value("${otp.method}")
    private String otpMethod;
    @Value("${otp.key}")
    private String otpKey;
    @Value("${otp.format}")
    private String otpFormat;
    @Value("${otp.sender}")
    private String otpSender;


    @Autowired
    private OtpDetailsRepository otpDetailsRepository;
    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

    public int generateCustOtp(com.enach.Models.CustomerDetails customerDetails) {

        String mobileNo = customerDetails.getMobileNo();
        int count = otpDetailsRepository.countMobileNo(mobileNo);

        if (count > 0) {

            String mobileNo1 = customerDetails.getMobileNo();
            otpDetailsRepository.deletePrevOtp(mobileNo1);
            logger.info("previous otp deleted");
        }
        int randomNo = (int)(Math.random()*900000)+100000;

        System.out.println("===RAMDOM==="+randomNo);
        return randomNo;
    }

    public boolean sendOtp(String mobileNo, int otpCode)
    {
        boolean status=false;
        String otpMsg="Your E-Nach Registration OTP is "+otpCode+" for Loan XXXXXXXXXXXXXX046174.\n" +
                "Regards\n" +
                "Shubham Housing Development Finance Company";

        String apiUrl=otpUrl+"?method="+otpMethod+"&api_key="+otpKey+"&to="+mobileNo+"&sender="+otpSender+"&message="+otpMsg+"&format="+otpFormat;

        RestTemplate restTemplate=new RestTemplate();
        HashMap<String,String> otpResponse=restTemplate.getForObject(apiUrl,HashMap.class);

        if(otpResponse.get("status").equals("OK"))
        {
            status=true;
        }
        return status;
    }


}
