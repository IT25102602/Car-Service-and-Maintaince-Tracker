package com.carservice.tracker.service;

import com.carservice.tracker.model.ServiceHistory;
import com.carservice.tracker.repository.ServiceHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceHistoryService {

    @Autowired
    private ServiceHistoryRepository repository;

    public void addService(ServiceHistory record) {
        repository.save(record);
    }

    public List<ServiceHistory> getAllServices() {
        return repository.findAll();
    }

    public List<ServiceHistory> searchServices(String query) {
        // Search in both vehicle number and customer name
        List<ServiceHistory> byVehicle = repository.findByVehicleNumberContaining(query);
        if (byVehicle.isEmpty()) {
            return repository.findByCustomerNameContaining(query);
        }
        return byVehicle;
    }

    public ServiceHistory getByServiceId(String serviceId) {
        return repository.findById(serviceId).orElse(null);
    }

    public void updateService(ServiceHistory updatedRecord) {
        repository.save(updatedRecord);
    }

    public void deleteService(String serviceId) {
        repository.deleteById(serviceId);
    }
}
