package com.enach.Repository;

import com.enach.Entity.EnachPayment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;


@Repository
public interface EnachPaymentRepository  extends JpaRepository<EnachPayment, Long> {


    @Query("select cd from EnachPayment cd where cd.transactionNo=:transactionNo")
    EnachPayment findByTansactionNo(@Param("transactionNo")String transactionNo);

    @Transactional
    @Modifying
    @Query("update EnachPayment cd set cd.transactionStatus=:transactionStatus, cd.transactionCompleteDate=:transactionCompleteDate, cd.errorMessage=:errorMessage where cd.transactionNo=:transactionNo")
    void updatePaymentStatus(@Param("transactionNo") String transactionNo, @Param("transactionStatus") String transactionStatus, @Param("errorMessage") String errorMessage, @Param("transactionCompleteDate")Timestamp transactionCompleteDate);

    @Query("select cd from EnachPayment cd where cd.transactionNo=:transactionNo")
    EnachPayment findLoanNoAndMandateType(@Param("transactionNo") String transactionNo);

    @Transactional
    @Modifying
    @Query("update EnachPayment cd set cd.transactionStatus='Failed', cd.errorMessage='Due to new request initiated' where cd.transactionStatus='inprocess' and cd.applicationNo=:applicationNo and cd.mandateType=:mandateType")
    void unprocessTransaction(String applicationNo, String mandateType);
}
