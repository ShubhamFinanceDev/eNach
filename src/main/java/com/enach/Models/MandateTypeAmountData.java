package com.enach.Models;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MandateTypeAmountData {

    private Double installmentAmount;
    private Double sanctionLoanAmount;

    public MandateTypeAmountData(Double installmentAmount, Double sanctionLoanAmount) {
        this.installmentAmount = installmentAmount;
        this.sanctionLoanAmount = sanctionLoanAmount;
    }

    public MandateTypeAmountData() {

    }
}
