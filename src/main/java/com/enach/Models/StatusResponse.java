package com.enach.Models;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatusResponse extends  CommonResponse{

    private String applicationNo;
    private String mandateType;
    private BigDecimal amount;

}
