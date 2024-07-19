package com.enach.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
@Entity
@Table(name = "enach_old")
@Data
public class CustomerDetails implements UserDetails {
    @Id
    @Column(name = "APPLICATION_NUMBER")
    private String applicationNumber;
    @Column(name = "BRANCH_NAME")
    private String branchName;
    @Column(name = "SANCTION_AMOUNT")
    private Double sanctionLoanAmount;
    @Column(name = "CUSTOMER_NAME")
    private String customerName;
//    @Column(name = "FIRST_DISBURSAL_DATE")
//    private LocalDate firstDisbursalDate;
//    @Column(name = "FIRST_INSTALLMENT_DATE")
//    private LocalDate firstInstalmentDate;
    @Column(name = "INSTALLMENT_AMOUNT")
    private Double installmentAmount;
//    @Column(name = "NEXT_DUE_DATE")
//    private LocalDate nextInstallmentDueDate;
    @Column(name = "CURRENT_STATUS")
    private String currentStatus;
    @Column(name = "Mobile_No")
    private String phoneNumber;
    @Column(name = "LOAN_ACCOUNT_NO")
    private String loanAccountNo;


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
