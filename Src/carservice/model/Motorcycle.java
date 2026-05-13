package com.carservice.model;

import jakarta.persistence.*;

/**
 * OOP: Inheritance – Motorcycle extends Vehicle
 * Polymorphism: overrides getServiceRecommendation() and getVehicleIcon()
 */
@Entity
@DiscriminatorValue("MOTORCYCLE")
public class Motorcycle extends Vehicle {

    @Column(name = "engine_cc")
    private Integer engineCc;

    @Column(name = "bike_type", length = 30)
    private String bikeType = "Sport";  // Sport | Cruiser | Touring | Dirt

    public Motorcycle() {
        setVehicleType("Motorcycle");
    }

    /**
     * Polymorphism: Motorcycle-specific service recommendation
     */
    @Override
    public String getServiceRecommendation() {
        if ("Service Due".equals(getStatus())) {
            return "Your motorcycle needs chain lubrication and brake inspection. Book a service now.";
        }
        if (getMileage() > 15000) {
            return "Time to check the chain, sprocket wear, and brake pads for safe riding.";
        }
        return "Keep your motorcycle in top shape with regular oil changes and chain maintenance.";
    }

    @Override
    public String getVehicleIcon() { return "🏍️"; }

    // Getters & Setters
    public Integer getEngineCc() { return engineCc; }
    public void setEngineCc(Integer engineCc) { this.engineCc = engineCc; }

    public String getBikeType() { return bikeType; }
    public void setBikeType(String bikeType) { this.bikeType = bikeType; }
}