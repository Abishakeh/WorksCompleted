package com.example.HRM;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AdminLoginRepo extends JpaRepository<AdminLoginService, Long> {
    Optional<AdminLoginService> findByUsername(String username);
}
