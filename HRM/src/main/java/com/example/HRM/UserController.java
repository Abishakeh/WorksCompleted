package com.example.HRM;


import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/HRM/api")
public class UserController {
    @Autowired
    UserService userService;
    private final UserRepo userRepo;



    @RequestMapping(value="/Admin/Employee/InsertCred")
    public String EmpCredInsert(Model Model1){
        UserLogin user = new UserLogin();
        Model1.addAttribute("user",user);
        return "EmployeeCredInsert";
    }

    @RequestMapping(value="/EmployeeCred/Update/{username}")
    public ModelAndView EmployeeCredUpdate(@PathVariable("username") String username){
        ModelAndView mav = new ModelAndView("EmployeeCredUpdate");
        UserLogin user = userService.editEmployee(username);
        mav.addObject("user",user);
        return mav;
    }

    @PostMapping
    @RequestMapping(path= "/Employee/add")
    public String saveEmployeeCred(@ModelAttribute("user") UserLogin userLogin){
        userService.addEmployeeCred(userLogin);
        userService.searchEmployeeCred();
        return "redirect:/HRM/api/Admin/Employee/LoginCred";
    }

    @RequestMapping(value="/EmployeeCred/Delete/{username}")
    public String DeleteEmployeeCred(@PathVariable(name="username") String username){
        userService.DeleteEmployeeCred(username);
        return "redirect:/HRM/api/Admin/Employee/LoginCred";
    }


    @Autowired
    public UserController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/Employee")
    public ModelAndView EmployeeLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session, Model Model1){
        Optional<UserLogin> user = userRepo.findByUsername(username);

        if(user.isPresent()&& user.get().getPassword().equals(password)){
            session.setAttribute("username",user.get().getUsername());
            return new ModelAndView("redirect:/HRM/api/EmployeeLogin");
        }
        Model1.addAttribute("error","Invalid username or password");
        return new ModelAndView("User");
    }

    @GetMapping("/Admin/Employee/LoginCred")
    public String UserCred(Model Model1){
        Model1.addAttribute("info",userService.searchEmployeeCred());
        return "EmployeeCred";
    }


}
