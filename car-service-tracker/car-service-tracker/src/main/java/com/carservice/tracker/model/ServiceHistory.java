package com.carservice.tracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "service_history")
public class ServiceHistory {
    @Id
    private String serviceId;
    private String vehicleNumber;
    private String customerName;
    private String serviceType;
    private String serviceDate;
    private String mechanicName;
    private double cost;
    private String notes;
    private String status;

    public ServiceHistory() {}

    public ServiceHistory(String serviceId, String vehicleNumber, String customerName, String serviceType, 
                          String serviceDate, String mechanicName, double cost, String notes, String status) {
        this.serviceId = serviceId;
        this.vehicleNumber = vehicleNumber;
        this.customerName = customerName;
        this.serviceType = serviceType;
        this.serviceDate = serviceDate;
        this.mechanicName = mechanicName;
        this.cost = cost;
        this.notes = notes;
        this.status = status;
    }

    // Getters and Setters
    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    public String getServiceDate() { return serviceDate; }
    public void setServiceDate(String serviceDate) { this.serviceDate = serviceDate; }
    public String getMechanicName() { return mechanicName; }
    public void setMechanicName(String mechanicName) { this.mechanicName = mechanicName; }
    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
