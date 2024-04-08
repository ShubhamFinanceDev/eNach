package com.enach.Models;

import jakarta.persistence.Column;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class EnachPaymentRequest {

    private String transactionNo;
    private String applicationNo;
    private String mandateType;
    private Timestamp transactionStartDate;
}
