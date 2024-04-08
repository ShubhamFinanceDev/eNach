package com.enach.Models;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class OtpVerifyResponse extends CommonResponse {

 /*   private String jwtToken;
    private String custName;
    private String loanNo;
    private String mobileNo;
    private String email;
    private LocalDate startDate;
    private LocalDate expiryDate;*/


    private String jwtToken;
    private String applicationNumber;
    private String customerNumber;
    private String loanAccountNo;
    private LocalDateTime startDate;
    private LocalDateTime expiryDate;
    private String currentStatus;
    private String mobileNo;
    private String customerName;



    public OtpVerifyResponse() {
        this.jwtToken = "";
        this.applicationNumber = "";
        this.customerNumber = "";
        this.loanAccountNo = "";
        this.startDate = null;
        this.expiryDate = null;
        this.currentStatus = "";
        this.mobileNo = "";
        this.customerName="";
    }
}
