package com.carservicetracker.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    // Show login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Handle login form submit
    @PostMapping("/login")
    public String doLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session
    ) {

        //  SIMPLE DEMO LOGIN (replace with DB later)

        if (username.equals("admin") && password.equals("admin123")) {

            session.setAttribute("role", "ADMIN");
            session.setAttribute("user", username);

            return "redirect:/admin/services";
        }

        else if (username.equals("customer") && password.equals("customer123")) {

            session.setAttribute("role", "CUSTOMER");
            session.setAttribute("user", username);

            return "redirect:/customer/services";
        }

        else {
            return "redirect:/login?error=true";
        }
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}