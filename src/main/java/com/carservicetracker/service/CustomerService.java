package com.carservicetracker.service;

import com.carservicetracker.model.Customer;
import com.carservicetracker.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    // ── Create ───────────────────────────────────────────────────
    public Customer saveCustomer(Customer customer) {
        return repository.save(customer);
    }

    // ── Read ─────────────────────────────────────────────────────
    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // ── Duplicate check ───────────────────────────────────
    // Returns error message if duplicate found, null if OK
    public String checkUpdateDuplicates(Long id, String newEmail, String newPhone) {

        // Check if new email is used by a DIFFERENT customer
        if (newEmail != null && !newEmail.isEmpty()) {
            Optional<Customer> found = repository.findByEmail(newEmail);
            if (found.isPresent() && !found.get().getId().equals(id)) {
                return "This email is already registered to another account.";
            }
        }

        // Check if new phone is used by a DIFFERENT customer
        if (newPhone != null && !newPhone.isEmpty()) {
            Optional<Customer> found = repository.findByPhone(newPhone);
            if (found.isPresent() && !found.get().getId().equals(id)) {
                return "This phone number is already registered to another account.";
            }
        }

        return null; // no duplicates
    }

    // ── Update ───────────────────────────────────────────────────
    public Customer updateCustomer(Long id, Customer updated) {
        Customer existing = getCustomerById(id);
        if (existing == null) return null;

        if (updated.getName() != null && !updated.getName().isEmpty())
            existing.setName(updated.getName());

        if (updated.getEmail() != null && !updated.getEmail().isEmpty())
            existing.setEmail(updated.getEmail());

        if (updated.getPhone() != null && !updated.getPhone().isEmpty())
            existing.setPhone(updated.getPhone());

        if (updated.getAddress() != null && !updated.getAddress().isEmpty())
            existing.setAddress(updated.getAddress());

        if (updated.getPassword() != null && !updated.getPassword().isEmpty())
            existing.setPassword(updated.getPassword());

        return repository.save(existing);
    }

    // ── Delete ───────────────────────────────────────────────────
    public void deleteCustomer(Long id) {
        repository.deleteById(id);
    }

    // ── Password check ───────────────────────────────────────────
    public boolean checkPassword(String rawPassword, String storedPassword) {
        return rawPassword != null && rawPassword.equals(storedPassword);
    }
}