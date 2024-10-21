package com.example.HRM;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("HRM/api")
public class AdminLoginController {
    private final AdminLoginRepo adminLoginRepo;

    @Autowired
    public AdminLoginController(AdminLoginRepo adminLoginRepo) {
        this.adminLoginRepo = adminLoginRepo;
    }

    @PostMapping("/Admin")
    public ModelAndView Login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session, Model Model1){
        Optional<AdminLoginService> user = adminLoginRepo.findByUsername(username);

        if(user.isPresent()&& user.get().getPassword().equals(password)){
            session.setAttribute("username",user.get().getUsername());
            session.setAttribute("role",user.get().getRole());
            return new ModelAndView("redirect:/HRM/api/Admin/Employee");
        }
        Model1.addAttribute("error","Invalid username or password");
        return new ModelAndView("/Admin");
    }
}
