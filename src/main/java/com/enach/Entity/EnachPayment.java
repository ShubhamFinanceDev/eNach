package com.enach.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "enach_payment")
@Data


public class EnachPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "transactionNo")
    private  String transactionNo;
    @Column(name = "applicationNo")
    private String applicationNo;
    @Column(name = "paymentMethod")
    private String paymentMethod;
    @Column(name = "transactionStartDate")
    private Timestamp transactionStartDate;
    @Column(name = "transactionCompleteDate")
    private Timestamp transactionCompleteDate;
    @Column(name = "transactionStatus")
    private String transactionStatus;
    @Column(name = "mandateType")
    private String mandateType;
    @Column(name = "errorMessage")
    private String errorMessage;
    @Column(name="amount")
    private BigDecimal amount;
    @Column(name="refrenceId")
    private String refrenceId;
    @Column(name="bankName")
    private String bankName;
    @Column(name="bankAccountNo")
    private String bankAccountNo;
    @Column(name="ifscCode")
    private String ifscCode;
    @Column(name="startDate")
    private Date startDate;
    @Column(name="endDate")
    private Date endDate;
    @Column(name="umrn")
    private String umrn;
    @Column(name = "registeredamount")
    private  String registeredAmount;






}
