package com.carservicetracker.service;

import com.carservicetracker.model.Customer;
import com.carservicetracker.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    public Customer saveCustomer(Customer customer) {
        return repository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Customer updateCustomer(Long id, Customer updated) {
        Customer existing = getCustomerById(id);
        if (existing == null) {
            return null;
        }

        // Update name if provided
        if (updated.getName() != null && !updated.getName().isEmpty()) {
            existing.setName(updated.getName());
        }

        // Update email if provided
        if (updated.getEmail() != null && !updated.getEmail().isEmpty()) {
            existing.setEmail(updated.getEmail());
        }

        // Update phone if provided
        if (updated.getPhone() != null && !updated.getPhone().isEmpty()) {
            existing.setPhone(updated.getPhone());
        }

        // Update address if provided
        if (updated.getAddress() != null && !updated.getAddress().isEmpty()) {
            existing.setAddress(updated.getAddress());
        }

        // Update password if provided
        if (updated.getPassword() != null && !updated.getPassword().isEmpty()) {
            existing.setPassword(updated.getPassword());
        }

        return repository.save(existing);
    }

    public void deleteCustomer(Long id) {
        repository.deleteById(id);
    }

    // Password check
    public boolean checkPassword(String rawPassword, String storedPassword) {
        return rawPassword != null && rawPassword.equals(storedPassword);
    }
}