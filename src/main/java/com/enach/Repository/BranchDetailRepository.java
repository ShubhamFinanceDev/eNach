package com.enach.Repository;

import com.enach.Entity.BranchDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchDetailRepository extends JpaRepository<BranchDetails, Long> {


    @Query("select  cd.emailId from BranchDetails cd where cd.branchName=:branchName")
    String findByBranchEmail(String branchName);
}
