package com.enach.Entity;

import jakarta.persistence.*;
import lombok.Data;


import java.util.Date;

@Entity
@Data
@Table(name = "customer_details")
public class CustomerDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cust_id")
    private Long custId;

    @Column(name = "cust_name")
    private String custName;

    @Column(name = "loan_no")
    private String loanNo;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "email")
    private String email;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "expiry_date")
    private Date expiryDate;

    @Column(name = "amount")
    private String amount;


}
