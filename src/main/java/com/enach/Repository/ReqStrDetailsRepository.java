package com.enach.Repository;

import com.enach.Entity.ResponseStructure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReqStrDetailsRepository extends JpaRepository<ResponseStructure,Long> {
}
