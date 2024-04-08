package com.enach.Models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtpRequest {
    private String MobileNo;
    private String otpCode;
    private String applicationNo;
}
