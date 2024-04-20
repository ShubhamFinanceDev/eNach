package com.enach.Models;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MandateTypeAmountResponse extends CommonResponse{

    private BigDecimal amount;
}
