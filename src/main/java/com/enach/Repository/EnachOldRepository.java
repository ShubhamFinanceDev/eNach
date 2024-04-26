package com.enach.Repository;

import com.enach.Entity.EnachOldCustomerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnachOldRepository extends JpaRepository<EnachOldCustomerDetails, Long> {

    @Query("select cd.applicationNo,cd.branchName,cd.sanctionAmount," +
            "   cd.customerName,DATE_FORMAT (STR_TO_DATE (cd.firstDisbursalDate," +
            " '%d-%m-%y'), '%Y-%m-%d'),DATE_FORMAT (STR_TO_DATE" +
            "    (cd.firstInstalmentDate, '%d-%m-%y'), '%Y-%m-%d'), " +
            "   cd.installmentAmount, DATE_FORMAT (STR_TO_DATE (cd.nextDueDate, " +
            "   '%d-%m-%y'), '%Y-%m-%d'),cd.mobileNo,cd.currentStatus from " +
            "   EnachOldCustomerDetails cd where cd.currentStatus='A' " +
            "   AND cd.applicationNo=:applicationNo")
    List<?> findOldCustomerDetails(@Param("applicationNo") String applicationNo);

    @Query("select cd.installmentAmount,cd.sanctionAmount" +
            "   FROM EnachOldCustomerDetails cd where cd.currentStatus='A' " +
            "   AND cd.applicationNo=:applicationNo")
    List<?> findMandateTypeAmount(String applicationNo);
}
