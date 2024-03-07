package com.enach.Entity;

import jakarta.persistence.*;
import lombok.Data;

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

    @Column(name = "loanNo")
    private String loanNo;

    @Column(name = "transactionStartDate")
    private Timestamp transactionStartDate;

    @Column(name = "transactionCompleteDate")
    private Timestamp transactionCompleteDate;

    @Column(name = "transactionStatus")
    private String transactionStatus;


}
