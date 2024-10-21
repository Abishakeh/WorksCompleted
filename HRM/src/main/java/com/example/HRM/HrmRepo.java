package com.example.HRM;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HrmRepo extends JpaRepository<Hrm, Long>{
    Hrm findByEmpID(Long empID);

}
