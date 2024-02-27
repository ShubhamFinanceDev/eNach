package com.enach.Repository;

import com.enach.Entity.OtpDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpDetailsRepository extends JpaRepository<OtpDetails,Long> {

    @Query("select COUNT(otp) from OtpDetails otp where otp.mobileNo=:mobileNo")
    int countMobileNo(String mobileNo);
    @Modifying
    @Transactional
    @Query("Delete from OtpDetails otp where otp.mobileNo=:mobileNo")
    void deletePrevOtp(String mobileNo);
    @Query("select otp from OtpDetails otp where otp.mobileNo=:mobileNo AND otp.otpCode=:otpCode")
    OtpDetails IsotpExpired(String mobileNo, String otpCode);
  /*  @Query("select otp from OtpDetails otp where otp.mobileNo=:mobileNo AND otp.otpCode=:otpCode AND TIMESTAMPDIFF(SECOND, '2024-02-26 13:31:37', NOW())<60")
    OtpDetails IsotpExpired(String mobileNo, String otpCode);*/

}
