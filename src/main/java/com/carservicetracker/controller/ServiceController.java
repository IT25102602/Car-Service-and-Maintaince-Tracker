package com.carservicetracker.controller;

import com.carservicetracker.model.Service;
import com.carservicetracker.repository.ServiceRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/services")
public class ServiceController {

    private final ServiceRepository repository;

    public ServiceController(ServiceRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String listServices(Model model,
                               @RequestParam(required = false) String search) {

        List<Service> services = repository.findAll();

        if (search != null && !search.isEmpty()) {
            services = services.stream()
                    .filter(s -> s.getName().toLowerCase().contains(search.toLowerCase()) ||
                            s.getDescription().toLowerCase().contains(search.toLowerCase()))
                    .toList();
        }

        model.addAttribute("services", services);
        model.addAttribute("search", search);

        return "services";     // ← Must be "services" (not customer/services)
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("service", new Service());
        return "service-form";
    }

    @PostMapping("/save")
    public String saveService(@ModelAttribute Service service) {
        repository.save(service);
        return "redirect:/services";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Service service = repository.findById(id);
        if (service == null) {
            return "redirect:/services";
        }
        model.addAttribute("service", service);
        return "service-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteService(@PathVariable int id) {
        repository.delete(id);
        return "redirect:/services";
    }
    @GetMapping("/catalog")
    public String customerServices(Model model) {
        List<Service> services = repository.findAll();
        model.addAttribute("services", services);
        return "customer-services";   // ← This must match the filename
    }
}