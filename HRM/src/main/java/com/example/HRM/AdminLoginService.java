package com.example.HRM;

import jakarta.persistence.*;

@Table(name="AdminLogin")
@Entity
public class AdminLoginService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AdminId;
    private String username;
    private String password;
    private String role;

    public AdminLoginService(){

    }

    public AdminLoginService(String username, String password, String role){
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Long getUserId(){
        return AdminId;
    }
    public void setUserId(Long AdminId){
        this.AdminId = AdminId;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getRole(){
        return role;
    }
    public void setRole(String role){
        this.role = role;
    }
}
