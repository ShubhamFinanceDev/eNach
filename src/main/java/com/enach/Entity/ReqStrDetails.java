package com.enach.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "req_structure_details")
@Data
public class ReqStrDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "req_id")
    private Long reqId;

    @Column(name = "check_sum")
    private String checkSum;

    @Column(name = "msg_id")
    private String msgId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_mobile_no")
    private String customerMobileNo;

    @Column(name = "customer_emailid")
    private String customerEmailId;

    @Column(name = "customer_account_no")
    private String customerAccountNo;

    @Column(name = "customer_startdate")
    private String customerStartDate;

    @Column(name = "customer_expirydate")
    private String customerExpiryDate;

    @Column(name = "customer_debitamount")
    private Double customerDebitAmount;

    @Column(name = "customer_maxamount")
    private Double customerMaxAmount;

    @Column(name = "customer_debitfrequency")
    private String customerDebitFrequency;

    @Column(name = "customer_instructedmemberid")
    private String customerInstructedMemberId;

    @Column(name = "short_code")
    private String shortCode;

    @Column(name = "customer_sequencetype")
    private String customerSequenceType;

    @Column(name = "merchant_category_code")
    private String merchantCategoryCode;

    @Column(name = "customer_reference1")
    private String customerReference1;

    @Column(name = "customer_reference2")
    private String customerReference2;

    @Column(name = "channel")
    private String channel;

    @Column(name = "utilcode")
    private String utilCode;

    @Column(name = "filler1")
    private String filler1;

    @Column(name = "filler2")
    private String filler2;

    @Column(name = "filler3")
    private String filler3;

    @Column(name = "filler4")
    private String filler4;

    @Column(name = "filler5")
    private String filler5;

    @Column(name = "filler6")
    private String filler6;

    @Column(name = "filler7")
    private String filler7;

    @Column(name = "filler8")
    private String filler8;

    @Column(name = "filler9")
    private String filler9;

    @Column(name = "filler10")
    private String filler10;


}


