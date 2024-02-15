package com.enach.Repository;


import com.enach.Entity.CustomerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDetailsRepository extends JpaRepository<CustomerDetails,Long> {

    @Query("select cd from CustomerDetails cd where cd.loanNo=:loanNo ")
    CustomerDetails findByLoanNo(@Param("loanNo")String loanNo);

    @Query("select cd from CustomerDetails cd where cd.mobileNo=:mobileNo")
    CustomerDetails findCustomerDetailByMobile(String mobileNo);
}
