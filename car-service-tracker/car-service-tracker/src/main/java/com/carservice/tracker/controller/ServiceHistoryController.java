package com.carservice.tracker.controller;

import com.carservice.tracker.model.ServiceHistory;
import com.carservice.tracker.service.ServiceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "*")
public class ServiceHistoryController {

    @Autowired
    private ServiceHistoryService service;

    @GetMapping
    public List<ServiceHistory> getAll() {
        return service.getAllServices();
    }

    @GetMapping("/search")
    public List<ServiceHistory> search(@RequestParam String query) {
        return service.searchServices(query);
    }

    @PostMapping
    public void add(@RequestBody ServiceHistory serviceHistory) {
        service.addService(serviceHistory);
    }

    @PutMapping
    public void update(@RequestBody ServiceHistory serviceHistory) {
        service.updateService(serviceHistory);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteService(id);
    }
}
