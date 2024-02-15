package com.enach.Repository;

import com.enach.Entity.ReqStrDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReqStrDetailsRepository extends JpaRepository<ReqStrDetails,Long> {

   @Query("SELECT pp FROM ReqStrDetails pp " +
           "WHERE pp.customerAccountNo=:customerAccountNo")
    ReqStrDetails findByReqStrDetailsByCustomerAccountNo(String customerAccountNo);
}
