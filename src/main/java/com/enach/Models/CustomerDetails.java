package com.enach.Models;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class CustomerDetails {

    private String custName;
    private String loanNo;
    private String mobileNo;
    private String email;
    private LocalDate startDate;
    private LocalDate expiryDate;
    private Double amount;


}
