package com.enach.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "enach_old")
@Data
public class EnachOldCustomerDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long custId;
    @Column(name = "APPLICATION_NUMBER")
    private String applicationNo;
    @Column(name = "BRANCH_NAME")
    private String branchName;
    @Column(name = "CUSTOMER_NAME")
    private String customerName;
    @Column(name = "LOAN_ACCOUNT_NO")
    private String loanAccountNo;
    @Column(name = "FIRST_DISBURSAL_DATE")
    private String firstDisbursalDate;
    @Column(name = "FIRST_INSTALLMENT_DATE")
    private String firstInstalmentDate;
    @Column(name = "INSTALLMENT_AMOUNT")
    private String installmentAmount;
    @Column(name = "SANCTION_AMOUNT")
    private BigDecimal sanctionAmount;
    @Column(name = "NEXT_DUE_DATE")
    private String nextDueDate;
    @Column(name = "CURRENT_STATUS")
    private String currentStatus;
    @Column(name = "Mobile_No")
    private String mobileNo;

}
