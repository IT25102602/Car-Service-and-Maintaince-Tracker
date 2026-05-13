package com.carservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * Base Vehicle entity – OOP: Encapsulation
 * Subclasses Car and Motorcycle extend this via @Inheritance
 */
@Entity
@Table(name = "vehicles")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vehicle_category", discriminatorType = DiscriminatorType.STRING)
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ── Core Fields ──
    @NotBlank(message = "Vehicle type is required")
    @Column(name = "vehicle_type", nullable = false, length = 30)
    private String vehicleType;   // Car | Motorcycle | Truck | Van

    @NotBlank(message = "Make is required")
    @Column(nullable = false, length = 50)
    private String make;

    @NotBlank(message = "Model is required")
    @Column(nullable = false, length = 50)
    private String model;

    @Min(1990) @Max(2030)
    @Column(nullable = false)
    private int year;

    @Min(0)
    @Column(name = "mileage", nullable = false)
    private int mileage;

    @Column(name = "vin", length = 17)
    private String vin;

    @Column(name = "license_plate", length = 20)
    private String licensePlate;

    @Column(name = "owner_name", length = 100)
    private String ownerName;

    @Column(name = "status", length = 20)
    private String status = "Active";   // Active | Service Due

    // ── Service Info ──
    @Column(name = "next_service")
    private String nextService;

    @Column(name = "next_service_km")
    private Integer nextServiceKm;

    @Column(name = "next_service_days")
    private Integer nextServiceDays;

    @Column(name = "service_progress")
    private Integer serviceProgress = 20;

    // ── Relationship ──
    @Column(name = "customer_id")
    private Long customerId;

    // ── Audit ──
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ── OOP: Polymorphism – overridden by subclasses ──
    public String getServiceRecommendation() {
        if ("Service Due".equals(status)) {
            return "Schedule a service soon to keep your vehicle in top shape.";
        }
        return "Your vehicle is in good condition. Keep up with regular maintenance.";
    }

    public String getVehicleIcon() { return "🚗"; }

    // ── Getters & Setters ──
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getMileage() { return mileage; }
    public void setMileage(int mileage) { this.mileage = mileage; }

    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNextService() { return nextService; }
    public void setNextService(String nextService) { this.nextService = nextService; }

    public Integer getNextServiceKm() { return nextServiceKm; }
    public void setNextServiceKm(Integer nextServiceKm) { this.nextServiceKm = nextServiceKm; }

    public Integer getNextServiceDays() { return nextServiceDays; }
    public void setNextServiceDays(Integer nextServiceDays) { this.nextServiceDays = nextServiceDays; }

    public Integer getServiceProgress() { return serviceProgress; }
    public void setServiceProgress(Integer serviceProgress) { this.serviceProgress = serviceProgress; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}