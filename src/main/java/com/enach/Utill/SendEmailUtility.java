package com.enach.Utill;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SendEmailUtility {

    @Autowired
    private JavaMailSender mailSender;
    @Async
    public void sendEmailWithAttachment(String to, byte[] attachmentData) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Enach payment report");
        helper.setText("Dear Sir/Mam \n\n\n\n\n Please find the attached eNach payment report. \n\n\n\n\n Regards\n Shubham Housing Finance. ");

        InputStreamSource attachmentSource = new ByteArrayResource(attachmentData);
        helper.addAttachment("Enach-payment-report.xlsx", attachmentSource);

        mailSender.send(message);
    }
}
