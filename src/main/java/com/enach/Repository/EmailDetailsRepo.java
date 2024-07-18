package com.enach.Repository;

import com.enach.Entity.EmailDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface EmailDetailsRepo extends JpaRepository<EmailDetails,Long> {

    @Modifying
    @Transactional
    @Query("update EmailDetails cd set cd.lastSending=:timestamp where cd.email=:email")
    void updateSendingTime(String email, Timestamp timestamp);
}
