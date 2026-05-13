package com.carservice.model;

import jakarta.persistence.*;

/**
 * OOP: Inheritance – Car extends Vehicle
 * Polymorphism: overrides getServiceRecommendation() and getVehicleIcon()
 */
@Entity
@DiscriminatorValue("CAR")
public class Car extends Vehicle {

    @Column(name = "num_doors")
    private Integer numDoors = 4;

    @Column(name = "fuel_type", length = 20)
    private String fuelType = "Petrol";  // Petrol | Diesel | Hybrid | Electric

    public Car() {
        setVehicleType("Car");
    }

    /**
     * Polymorphism: Car-specific service recommendation
     */
    @Override
    public String getServiceRecommendation() {
        if ("Service Due".equals(getStatus())) {
            return "Your car needs an oil change and inspection. Book a service to avoid engine damage.";
        }
        if (getMileage() > 40000) {
            return "Consider a full tune-up. High mileage cars benefit from periodic inspections.";
        }
        return "Regular oil changes every 5,000 km keep your car engine healthy and fuel-efficient.";
    }

    @Override
    public String getVehicleIcon() { return "🚗"; }

    // Getters & Setters
    public Integer getNumDoors() { return numDoors; }
    public void setNumDoors(Integer numDoors) { this.numDoors = numDoors; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
}