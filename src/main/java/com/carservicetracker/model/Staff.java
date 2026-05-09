package com.carservicetracker.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "staff")
@Data
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.STAFF;

    private Boolean isActive = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Role {
        ADMIN, MECHANIC, STAFF
    }
}