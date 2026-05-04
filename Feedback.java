package com.carservicetracker.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks")
@Data
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long serviceId;

    private String customerName;
    private String vehiclePlate;

    private int overallRating;      // 1-5
    private int qualityRating;
    private int timelinessRating;
    private int staffRating;

    @Column(length = 1000)
    private String comments;

    private LocalDateTime feedbackDate = LocalDateTime.now();

    private String status = "Pending";
}
