package com.example.HRM;

import jakarta.persistence.*;

@Table(name="emp_info")
@Entity
public class Hrm {
    @SequenceGenerator(name="Hrm_Sequence", sequenceName="Hrm_sequence", allocationSize =1 )

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="Hrm_Sequence")
    private Long empID;
    private String empName;
    private int NoOfLeaves;
    private String empStatus;
    private int contactNo;
    private String email;
    private String joiningDate;
    private String description;
    private String leaveDate;
    private String password;


    public Hrm(Long empID, String empName, int noOfLeaves, String empStatus, int contactNo, String email, String joiningDate, String description, String leaveDate, String password) {
        this.empID = empID;
        this.empName = empName;
        NoOfLeaves = noOfLeaves;
        this.empStatus = empStatus;
        this.contactNo = contactNo;
        this.email = email;
        this.joiningDate = joiningDate;
        this.description = description;
        this.leaveDate = leaveDate;
        this.password = password;
    }

    public Hrm(){

    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public String getLeaveDate(){
        return leaveDate;
    }

    public void setLeaveDate(String LeaveDate){
        this.leaveDate = LeaveDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getContactNo() {
        return contactNo;
    }

    public void setContactNo(int contact_No) {
        this.contactNo = contact_No;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Long getEmpID(){
        return empID;
    }
    public void setEmpID(Long E){
        this.empID = E;
    }
    public String getEmpName(){
        return empName;
    }
    public void setEmpName(String En){
        this.empName = En;
    }
    public int getNoOfLeaves(){
        return NoOfLeaves;
    }
    public void setNoOfLeaves(int NOL){
        this.NoOfLeaves = NOL;
    }
    public String getEmpStatus(){
        return empStatus;
    }
    public void setEmpStatus(String EMS){
        this.empStatus = EMS;
    }
}
