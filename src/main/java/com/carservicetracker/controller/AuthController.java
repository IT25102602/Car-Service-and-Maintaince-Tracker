package com.carservicetracker.controller;

import com.carservicetracker.model.Customer;
import com.carservicetracker.model.Staff;
import com.carservicetracker.repository.CustomerRepository;
import com.carservicetracker.repository.StaffRepository;
import com.carservicetracker.service.CustomerService;
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

    @Autowired
    private CustomerService customerService;

    // ── Register Customer ────────────────────────────────────────
    @PostMapping("/register/customer")
    public ResponseEntity<?> registerCustomer(@RequestBody Customer customer) {
        Map<String, Object> response = new HashMap<>();

        // Check duplicate email
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            response.put("success", false);
            response.put("message", "An account with this email already exists.");
            return ResponseEntity.badRequest().body(response);
        }

        // Validate required fields
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

        // Email must be @gmail.com
        if (!customer.getEmail().toLowerCase().endsWith("@gmail.com")) {
            response.put("success", false);
            response.put("message", "Email must be a @gmail.com address.");
            return ResponseEntity.badRequest().body(response);
        }

        // Phone must be exactly 10 digits (if provided)
        if (customer.getPhone() != null && !customer.getPhone().isEmpty()) {
            if (!customer.getPhone().matches("\\d{10}")) {
                response.put("success", false);
                response.put("message", "Phone number must be exactly 10 digits.");
                return ResponseEntity.badRequest().body(response);
            }
        }

        // Set defaults
        customer.setCreatedAt(LocalDateTime.now());
        if (customer.getMembershipType() == null || customer.getMembershipType().isEmpty()) {
            customer.setMembershipType("Regular");
        }

        try {
            // saveCustomer() hashes the password automatically via BCrypt
            customerService.saveCustomer(customer);
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

        if (email == null || password == null || role == null) {
            response.put("success", false);
            response.put("message", "Email, password and role are required.");
            return ResponseEntity.badRequest().body(response);
        }

        if ("customer".equals(role)) {
            Customer customer = customerRepository.findByEmail(email).orElse(null);

            // Use BCrypt checkPassword — compares raw input against stored hash
            if (customer != null && customerService.checkPassword(password, customer.getPassword())) {
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

            if (staff != null && staff.getPassword() != null
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