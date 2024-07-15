package com.enach.Repository;

import com.enach.Entity.CustomerDetails;
import com.enach.Models.BranchNameDetail;
import com.enach.Models.MandateTypeAmountData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDetailsRepository extends JpaRepository<com.enach.Entity.CustomerDetails,Long> {

    @Query("select cd from CustomerDetails cd where cd.applicationNumber=:applicationNo")
    CustomerDetails getCustomerDetailsBy(String applicationNo);
    @Query("select new com.enach.Models.BranchNameDetail (cd.branchName,cd.applicationNumber) from CustomerDetails cd where cd.applicationNumber=:applicationNo")
    BranchNameDetail getBranchName(String applicationNo);
    @Query("select new com.enach.Models.MandateTypeAmountData (cd.installmentAmount,cd.sanctionLoanAmount) from CustomerDetails cd where cd.applicationNumber=:applicationNo")
    MandateTypeAmountData getMandateTypeAmount(String applicationNo);
}
