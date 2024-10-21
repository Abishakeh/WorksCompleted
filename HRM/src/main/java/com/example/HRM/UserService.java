package com.example.HRM;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    public List<UserLogin> searchEmployeeCred(){
        return userRepo.findAll();
    }

    public UserLogin editEmployee(String username){
        return userRepo.findById(username).get();
    }

    public void addEmployeeCred(UserLogin user){
        userRepo.save(user);
    }

    public void DeleteEmployeeCred(String username){
        userRepo.deleteById(username);
    }
}
