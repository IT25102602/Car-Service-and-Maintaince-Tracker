package com.carservice.service;

import com.carservice.model.*;
import com.carservice.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Layer – Business Logic
 * OOP: Encapsulation (all DB access goes through this service)
 * Implements all 4 CRUD operations
 */
@Service
@Transactional
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    /* ═══════════════════════════════════════
       CREATE – Add new vehicle
    ═══════════════════════════════════════ */
    public Vehicle createVehicle(Vehicle vehicle) {
        // OOP: Factory-style creation based on type
        Vehicle entity = buildTypedVehicle(vehicle);
        copyFields(vehicle, entity);
        return vehicleRepository.save(entity);
    }

    /* ═══════════════════════════════════════
       READ – Get all vehicles for a customer
    ═══════════════════════════════════════ */
    @Transactional(readOnly = true)
    public List<Vehicle> getVehiclesByCustomer(Long customerId) {
        return vehicleRepository.findByCustomerId(customerId);
    }

    /* ═══════════════════════════════════════
       READ – Get single vehicle by ID
    ═══════════════════════════════════════ */
    @Transactional(readOnly = true)
    public Optional<Vehicle> getVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }

    /* ═══════════════════════════════════════
       READ – Search vehicles
    ═══════════════════════════════════════ */
    @Transactional(readOnly = true)
    public List<Vehicle> searchVehicles(Long customerId, String query) {
        if (query == null || query.isBlank()) {
            return vehicleRepository.findByCustomerId(customerId);
        }
        return vehicleRepository.searchByCustomer(customerId, query.trim());
    }

    /* ═══════════════════════════════════════
       READ – Get all vehicles (admin)
    ═══════════════════════════════════════ */
    @Transactional(readOnly = true)
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    /* ═══════════════════════════════════════
       UPDATE – Edit vehicle details
    ═══════════════════════════════════════ */
    public Vehicle updateVehicle(Long id, Vehicle updated) {
        Vehicle existing = vehicleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));

        copyFields(updated, existing);

        // Update mileage & recompute service status
        if (updated.getMileage() > 0) {
            existing.setMileage(updated.getMileage());
            recomputeServiceStatus(existing);
        }

        return vehicleRepository.save(existing);
    }

    /* ═══════════════════════════════════════
       DELETE – Remove vehicle record
    ═══════════════════════════════════════ */
    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new RuntimeException("Vehicle not found with id: " + id);
        }
        vehicleRepository.deleteById(id);
    }

    /* ═══════════════════════════════════════
       OOP: Polymorphism – service recommendation
    ═══════════════════════════════════════ */
    public String getServiceRecommendation(Long vehicleId) {
        Vehicle v = vehicleRepository.findById(vehicleId)
            .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        // Polymorphic call – Car and Motorcycle return different messages
        return v.getServiceRecommendation();
    }

    /* ── Private Helpers ── */

    /**
     * OOP: Factory pattern – creates typed Vehicle subclass based on vehicleType field
     */
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
        if (src.getVehicleType()    != null) dst.setVehicleType(src.getVehicleType());
        if (src.getMake()           != null) dst.setMake(src.getMake());
        if (src.getModel()          != null) dst.setModel(src.getModel());
        if (src.getYear()           > 0)     dst.setYear(src.getYear());
        if (src.getMileage()        >= 0)    dst.setMileage(src.getMileage());
        if (src.getVin()            != null) dst.setVin(src.getVin());
        if (src.getLicensePlate()   != null) dst.setLicensePlate(src.getLicensePlate());
        if (src.getOwnerName()      != null) dst.setOwnerName(src.getOwnerName());
        if (src.getStatus()         != null) dst.setStatus(src.getStatus());
        if (src.getNextService()    != null) dst.setNextService(src.getNextService());
        if (src.getNextServiceKm()  != null) dst.setNextServiceKm(src.getNextServiceKm());
        if (src.getNextServiceDays()!= null) dst.setNextServiceDays(src.getNextServiceDays());
        if (src.getServiceProgress()!= null) dst.setServiceProgress(src.getServiceProgress());
        if (src.getCustomerId()     != null) dst.setCustomerId(src.getCustomerId());
    }

    private void recomputeServiceStatus(Vehicle v) {
        // Simple heuristic: if next service KM is less than 1000, mark Service Due
        if (v.getNextServiceKm() != null && v.getNextServiceKm() < 1000) {
            v.setStatus("Service Due");
            v.setServiceProgress(90);
        }
    }
}