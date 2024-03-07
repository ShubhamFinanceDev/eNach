package com.enach.Models;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class EnachPaymentStatusRequest {

    private String transactionStatus;

}
