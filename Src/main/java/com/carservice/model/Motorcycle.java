package com.carservice.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("MOTORCYCLE")
public class Motorcycle extends Vehicle {

    @Column(name = "engine_cc")
    private Integer engineCc;

    @Column(name = "bike_type", length = 30)
    private String bikeType = "Sport";

    public Motorcycle() {
        setVehicleType("Motorcycle");
    }

    @Override
    public String getServiceRecommendation() {
        if ("Service Due".equals(getStatus())) {
            return "Your motorcycle needs chain lubrication and brake inspection. Book now.";
        }
        return "Keep your motorcycle in top shape with regular oil and chain maintenance.";
    }

    public Integer getEngineCc() { return engineCc; }
    public void setEngineCc(Integer engineCc) { this.engineCc = engineCc; }
    public String getBikeType() { return bikeType; }
    public void setBikeType(String bikeType) { this.bikeType = bikeType; }
}