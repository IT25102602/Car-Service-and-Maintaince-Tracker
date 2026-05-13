package com.carservice.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("CAR")
public class Car extends Vehicle {

    @Column(name = "num_doors")
    private Integer numDoors = 4;

    @Column(name = "fuel_type", length = 20)
    private String fuelType = "Petrol";

    public Car() {
        setVehicleType("Car");
    }

    @Override
    public String getServiceRecommendation() {
        if ("Service Due".equals(getStatus())) {
            return "Your car needs an oil change and inspection. Book a service now.";
        }
        return "Regular oil changes every 5,000 km keep your car engine healthy.";
    }

    public Integer getNumDoors() { return numDoors; }
    public void setNumDoors(Integer numDoors) { this.numDoors = numDoors; }
    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
}