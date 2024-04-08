package com.enach.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

@Data
public class CustomerDetails implements UserDetails {

   /* private String custId;
    private String custName;
    private String loanNo;
    private String mobileNo;
    private String email;
    private LocalDate startDate;
    private LocalDate expiryDate;
    private String amount;*/

    private String applicationNumber;
    private String customerNumber;
    private String loanAccountNo;
    private LocalDateTime firstDisbursalDate;
    private LocalDateTime firstInstalmentDate;
    private Double installmentAmount;
    private Double sanctionAmount;
    private String currentStatus;
    private String mobileNo;
    private String customerName;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.getApplicationNumber();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
