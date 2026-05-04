package com.carservicetracker.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "service_records")
@Data
public class ServiceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vehiclePlate;
    private String customerName;
    private LocalDate serviceDate;
    private String serviceType;
    private String status;           // Completed, In Progress
    private double totalCost;
    private String technicianNotes;
}
