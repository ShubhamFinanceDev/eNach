package com.enach.Repository;

import com.enach.Entity.StatusManage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<StatusManage,Long> {

    @Query("SELECT COUNT(s) > 0 FROM StatusManage s WHERE s.applicationNo = :applicationNo AND s.cancelCause LIKE %:cancelWord%")
    boolean existsByApplicationNoAndCancelCause(@Param("applicationNo") String applicationNo, @Param("cancelWord") String cancelWord);
}