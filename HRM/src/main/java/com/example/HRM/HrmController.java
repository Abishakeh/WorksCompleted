package com.example.HRM;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/HRM/api")
public class HrmController {
    @Autowired
    HrmService HrmService;


    @Autowired
    HrmRepo hrmRepo;

    @GetMapping("/Selection")
    public String LoginSelection(Model Model1){
        Model1.addAttribute("version"," The version is 1.0.1");
        return "selection";
    }

    @GetMapping("/Admin")
    public String AdminLogin(Model Model1){
        Model1.addAttribute("version"," The version is 1.0.1");
        return "Admin";
    }

    @GetMapping("/Employee")
    public String UserLogin(Model Model1){
        Model1.addAttribute("version"," The version is 1.0.1");
        return "User";
    }

    @GetMapping("/EmployeeLogin")
    public String EmployeeLogin(Model Model1){
        Model1.addAttribute("information",HrmService.searchEmployeeList());
        return "EmployeeLogin";
    }

    @GetMapping("/Admin/EmployeeSearch")
    public String EmployeeSearch(Model Model1){
        Model1.addAttribute("information",HrmService.searchEmployeeList());
        return "EmployeeSearch";
    }



    @GetMapping("/Employee/search")
    public String search(@RequestParam("EmployeeID")Long empID, @RequestParam("password") String password, HttpSession session, Model Model1){
        Hrm employee = hrmRepo.findByEmpID(empID);
        Optional<Hrm> user = hrmRepo.findById(empID);
        if(employee == null){
            Model1.addAttribute("message","No employees is found with this ID "+empID);
            return "EmployeeLeave";
        }
        if(user.isPresent()&& user.get().getPassword().equals(password)){
            session.setAttribute("empID",user.get().getEmpID());
            Model1.addAttribute("employee",employee);
            return "EmployeeLeave";
        }
        return "EmployeeLeave";
    }

    @GetMapping("/search")
    public String searchEmployee(@RequestParam("EmployeeID")Long empID, @RequestParam("password") String password, HttpSession session, Model Model1){
        Hrm employee = hrmRepo.findByEmpID(empID);
        Optional<Hrm> user = hrmRepo.findById(empID);
        if(employee == null){
            Model1.addAttribute("message","Mo employees is found with this ID "+empID);
            return "EmployeeLeave";
        }
        if(user.isPresent()&& user.get().getPassword().equals(password)){
            session.setAttribute("empID",user.get().getEmpID());
            Model1.addAttribute("employee",employee);
            return "SearchResult";
        }

        return "SearchResult";
    }


    @GetMapping("/Admin/Employee")
    public String AdminControl(Model Model1){
        Model1.addAttribute("information",HrmService.searchEmployeeList());
        return "EmployeeInformation";
    }

   @RequestMapping(value="/Admin/Employee/Insert")
        public String EmpInsert(Model Model1){
        Hrm hrm = new Hrm();
        Model1.addAttribute("HR",hrm);
        return "Insert";
    }
    
    @RequestMapping(value="/EmployeeUpdate/{empID}")
    public ModelAndView LeaveInsert(@PathVariable("empID") Long empID){
        ModelAndView mav = new ModelAndView("EmployeeUpdate");
        Hrm hrm = HrmService.editEmployee(empID);
        mav.addObject("HR",hrm);
        return mav;
    }

    @PostMapping
    @RequestMapping(path= "/add")
    public String saveEmployee(@ModelAttribute("HR") Hrm hrm){
        HrmService.addEmployee(hrm);
        HrmService.searchEmployeeList();
        return "redirect:/HRM/api/Admin/Employee";
    }

    @PostMapping
    @RequestMapping(path= "/Leave/add")
    public String saveEmployeeLeave(@ModelAttribute("HR") Hrm hrm){
        HrmService.addEmployee(hrm);
        HrmService.searchEmployeeList();
        return "redirect:/HRM/api/EmployeeLogin";
    }

    @RequestMapping(value="/Admin/Employee/Update/{id}")
    public ModelAndView EmpUpdate(@PathVariable("id") Long empID){
        ModelAndView mav = new ModelAndView("Update");
        Hrm hrm = HrmService.editEmployee(empID);
        mav.addObject("HR",hrm);
        return mav;
    }

    @GetMapping
    @RequestMapping(path ="/get/{id}")
    public Hrm getEmployee(@PathVariable("id")Long id){
        return HrmService.editEmployee(id);
    }
    @RequestMapping(value="/Admin/Employee/Delete/{id}")
    public String EmpDelete(@PathVariable(name="id") Long empID){
        HrmService.deleteEmployee(empID);
        return "redirect:/HRM/api/Admin/Employee";
    }

    @GetMapping
    @RequestMapping(path="/delete/{id}")
    public List<Hrm> deleteEmployee(@PathVariable("id")Long id){
        HrmService.deleteEmployee(id);
        return HrmService.searchEmployeeList();
    }



}
