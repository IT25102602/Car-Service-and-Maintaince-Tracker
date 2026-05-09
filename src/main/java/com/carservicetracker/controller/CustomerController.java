package com.carservicetracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @GetMapping("/services")
    public String customerServices() {
        return "customer/services";
    }

    @GetMapping("/dashboard")
    public String customerDashboard() {
        return "customer/dashboard";
    }
}