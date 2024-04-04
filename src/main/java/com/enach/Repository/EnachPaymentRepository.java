package com.enach.Repository;

import com.enach.Entity.EnachPayment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;


@Repository
public interface EnachPaymentRepository  extends JpaRepository<EnachPayment, Long> {


    @Query("select cd from EnachPayment cd where cd.transactionNo=:transactionNo")
    EnachPayment findByTansactionNo(@Param("transactionNo")String transactionNo);

    @Transactional
    @Modifying
    @Query("update EnachPayment cd set cd.transactionStatus=:transactionStatus, cd.transactionCompleteDate=:transactionCompleteDate, cd.errorMessage=:errorMessage where cd.transactionNo=:transactionNo")
    void updatePaymentStatus(@Param("transactionNo") String transactionNo, @Param("transactionStatus") String transactionStatus, @Param("errorMessage") String errorMessage, @Param("transactionCompleteDate")Timestamp transactionCompleteDate);

    @Query("select cd.mandateType,cd.loanNo from EnachPayment cd where cd.transactionNo=:transactionNo")
    List<?> findLoanNoAndMandateType(@Param("transactionNo") String transactionNo);


}
