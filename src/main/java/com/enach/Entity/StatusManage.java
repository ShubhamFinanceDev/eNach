package com.enach.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "status_mange")
public class StatusManage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "status_id")
    private Long statusId;
    @Column(name = "loan_no")
    private String loanNo;
    @Column(name = "application_no")
    private String applicationNo;
    @Column(name = "cancellation_time")
    private Timestamp cancellationTime;
    @Column(name = "cancel_cause")
    private String cancelCause;
}