package com.carservice.service;

import com.carservice.model.*;
import com.carservice.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public Vehicle createVehicle(Vehicle vehicle) {
        Vehicle entity = buildTypedVehicle(vehicle);
        copyFields(vehicle, entity);
        return vehicleRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<Vehicle> getVehiclesByCustomer(Long customerId) {
        return vehicleRepository.findByCustomerId(customerId);
    }

    @Transactional(readOnly = true)
    public Optional<Vehicle> getVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Vehicle> searchVehicles(Long customerId, String query) {
        if (query == null || query.isBlank()) {
            return vehicleRepository.findByCustomerId(customerId);
        }
        return vehicleRepository.searchByCustomer(customerId, query.trim());
    }

    @Transactional(readOnly = true)
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle updateVehicle(Long id, Vehicle updated) {
        Vehicle existing = vehicleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
        copyFields(updated, existing);
        return vehicleRepository.save(existing);
    }

    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new RuntimeException("Vehicle not found with id: " + id);
        }
        vehicleRepository.deleteById(id);
    }

    public String getServiceRecommendation(Long vehicleId) {
        Vehicle v = vehicleRepository.findById(vehicleId)
            .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        return v.getServiceRecommendation();
    }

    private Vehicle buildTypedVehicle(Vehicle v) {
        String type = v.getVehicleType();
        if (type == null) return new Vehicle();
        return switch (type) {
            case "Car"        -> new Car();
            case "Motorcycle" -> new Motorcycle();
            default           -> new Vehicle();
        };
    }

    private void copyFields(Vehicle src, Vehicle dst) {
        if (src.getVehicleType()     != null) dst.setVehicleType(src.getVehicleType());
        if (src.getMake()            != null) dst.setMake(src.getMake());
        if (src.getModel()           != null) dst.setModel(src.getModel());
        if (src.getYear()            > 0)     dst.setYear(src.getYear());
        if (src.getMileage()         >= 0)    dst.setMileage(src.getMileage());
        if (src.getVin()             != null) dst.setVin(src.getVin());
        if (src.getLicensePlate()    != null) dst.setLicensePlate(src.getLicensePlate());
        if (src.getOwnerName()       != null) dst.setOwnerName(src.getOwnerName());
        if (src.getStatus()          != null) dst.setStatus(src.getStatus());
        if (src.getNextService()     != null) dst.setNextService(src.getNextService());
        if (src.getNextServiceKm()   != null) dst.setNextServiceKm(src.getNextServiceKm());
        if (src.getNextServiceDays() != null) dst.setNextServiceDays(src.getNextServiceDays());
        if (src.getServiceProgress() != null) dst.setServiceProgress(src.getServiceProgress());
        if (src.getCustomerId()      != null) dst.setCustomerId(src.getCustomerId());
    }
}