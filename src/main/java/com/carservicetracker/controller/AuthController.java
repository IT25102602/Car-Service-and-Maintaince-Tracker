package com.carservicetracker.controller;

import com.carservicetracker.model.Customer;
import com.carservicetracker.model.Staff;
import com.carservicetracker.repository.CustomerRepository;
import com.carservicetracker.repository.StaffRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StaffRepository staffRepository;

    // ── Register Customer ────────────────────────────────────────
    @PostMapping("/register/customer")
    public ResponseEntity<?> registerCustomer(@RequestBody Customer customer) {
        Map<String, Object> response = new HashMap<>();

        // FIX 1: Check if email already exists
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            response.put("success", false);
            response.put("message", "An account with this email already exists.");
            return ResponseEntity.badRequest().body(response);
        }

        // FIX 2: Validate required fields are not empty
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Name is required.");
            return ResponseEntity.badRequest().body(response);
        }

        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Email is required.");
            return ResponseEntity.badRequest().body(response);
        }

        if (customer.getPassword() == null || customer.getPassword().trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Password is required.");
            return ResponseEntity.badRequest().body(response);
        }

        // FIX 3: Set createdAt timestamp so it never crashes on non-nullable column
        customer.setCreatedAt(LocalDateTime.now());

        // Set default membership type if not provided
        if (customer.getMembershipType() == null || customer.getMembershipType().isEmpty()) {
            customer.setMembershipType("Regular");
        }

        try {
            customerRepository.save(customer);
            response.put("success", true);
            response.put("message", "Customer registered successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // ── Register Staff ───────────────────────────────────────────
    @PostMapping("/register/staff")
    public ResponseEntity<?> registerStaff(@RequestBody Staff staff) {
        Map<String, Object> response = new HashMap<>();

        // Check if email already exists
        if (staffRepository.findByEmail(staff.getEmail()).isPresent()) {
            response.put("success", false);
            response.put("message", "An account with this email already exists.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            staffRepository.save(staff);
            response.put("success", true);
            response.put("message", "Staff registered successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // ── Login ────────────────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData, HttpSession session) {
        String email    = loginData.get("email");
        String password = loginData.get("password");
        String role     = loginData.get("role");

        Map<String, Object> response = new HashMap<>();

        // Validate input
        if (email == null || password == null || role == null) {
            response.put("success", false);
            response.put("message", "Email, password and role are required.");
            return ResponseEntity.badRequest().body(response);
        }

        if ("customer".equals(role)) {
            Customer customer = customerRepository.findByEmail(email).orElse(null);

            if (customer != null
                    && customer.getPassword() != null
                    && customer.getPassword().equals(password)) {

                session.setAttribute("user", customer);
                session.setAttribute("role", "customer");

                response.put("success", true);
                response.put("role", "customer");
                response.put("name", customer.getName());
                response.put("id", customer.getId());
                return ResponseEntity.ok(response);
            }

        } else if ("admin".equals(role)) {
            Staff staff = staffRepository.findByEmail(email).orElse(null);

            if (staff != null
                    && staff.getPassword() != null
                    && staff.getPassword().equals(password)) {

                session.setAttribute("user", staff);
                session.setAttribute("role", "admin");

                response.put("success", true);
                response.put("role", "admin");
                response.put("name", staff.getName());
                response.put("id", staff.getId());
                return ResponseEntity.ok(response);
            }
        }

        response.put("success", false);
        response.put("message", "Invalid email or password.");
        return ResponseEntity.badRequest().body(response);
    }

    // ── Logout ───────────────────────────────────────────────────
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }


}