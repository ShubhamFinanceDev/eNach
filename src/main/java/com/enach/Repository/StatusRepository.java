package com.enach.Repository;

import com.enach.Entity.StatusManage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<StatusManage,Long> {

}