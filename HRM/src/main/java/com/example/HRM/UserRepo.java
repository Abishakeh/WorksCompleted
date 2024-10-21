package com.example.HRM;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserLogin, String> {
    Optional<UserLogin> findByUsername(String username);

}
