package com.enach.Utill;


import com.enach.Models.EmailDetails;
import com.enach.Repository.OtpDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    public boolean sendOtp(String applicationNo, String mobileNo, int otpCode)
    {
        boolean status=false;

        String subStringapplicationNo= applicationNo.substring(applicationNo.length()-5,applicationNo.length());
        String otpMsg="Your E-Nach Registration OTP is "+otpCode+" for Loan XXXXXXXXXXXXXX"+subStringapplicationNo+"\n" +
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


    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;

    public void sendSimpleMail(EmailDetails emailDetails) {

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setText(emailDetails.getMsgBody());
            mailMessage.setSubject(emailDetails.getSubject());

            javaMailSender.send(mailMessage);

        } catch (Exception e) {
            System.out.println(e);
        }
    }



}
