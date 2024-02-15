package com.enach.Models;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;

@Data
//@Builder
public class OtpVerifyResponse extends CommonResponse {

    private String custName;
    private String loanNo;
    private String mobileNo;
    private String email;
    private Date startDate;
    private Date expiryDate;
    private String amount;



    public OtpVerifyResponse() {
        this.custName = "";
        this.loanNo = "";
        this.mobileNo = "";
        this.email = "";
        this.startDate = null;
        this.expiryDate = null;
        this.amount = "";
    }
}
