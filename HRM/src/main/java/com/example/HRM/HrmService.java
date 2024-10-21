package com.example.HRM;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HrmService {
    @Autowired
    HrmRepo hrmRepo;

    public void addEmployee(Hrm hrm){
        hrmRepo.save(hrm);
    }

    public Hrm editEmployee(Long empID){
        return hrmRepo.findById(empID).get();
    }

    public void deleteEmployee(Long ID){
        hrmRepo.deleteById(ID);
    }
    public Object searchEmployee(Long empID){
        return hrmRepo.getReferenceById(empID);
    }
    public List<Hrm> searchEmployeeList(){
        return hrmRepo.findAll();
    }
}