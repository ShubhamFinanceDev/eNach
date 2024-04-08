package com.enach.Models;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class OtpVerifyResponse extends CommonResponse {

    private String jwtToken;
    private String custName;
    private String applicationNo;
    private String mobileNo;
    private LocalDateTime startDate;
    private LocalDateTime expiryDate;


}
