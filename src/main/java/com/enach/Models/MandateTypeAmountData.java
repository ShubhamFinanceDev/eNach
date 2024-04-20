package com.enach.Models;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MandateTypeAmountData {

    private BigDecimal installmentAmount;
    private BigDecimal sanctionAmount;
}
