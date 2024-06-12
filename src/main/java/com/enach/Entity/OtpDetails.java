package com.enach.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "otp_detail")
@Data
public class OtpDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="otp_id")
    private Long otpId;
    @Column(name="mobile_no")
    private String mobileNo;
    @Column(name="otp_code")
    private Long otpCode;
    @Column(name="expr_time")
    private LocalDateTime otpExprTime=LocalDateTime.now();


}
