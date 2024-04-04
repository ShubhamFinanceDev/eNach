package com.enach.Models;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class OtpVerifyResponse extends CommonResponse {

    private String jwtToken;
    private String custName;
    private String loanNo;
    private String mobileNo;
    private String email;
    private LocalDate startDate;
    private LocalDate expiryDate;



    public OtpVerifyResponse() {
        this.jwtToken = "";
        this.custName = "";
        this.loanNo = "";
        this.mobileNo = "";
        this.email = "";
        this.startDate = null;
        this.expiryDate = null;
    }
}
