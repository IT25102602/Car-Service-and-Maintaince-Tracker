package com.carService.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

    @Entity
    @Table(name = "bookings")
    @Getter @Setter @NoArgsConstructor   // Lombok handles getters/setters for you
    public class Booking {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private Long customerId;
        private Long vehicleId;
        private String serviceType;
        private String bookingDate;
        private String bookingTime;
        private String status = "Pending";
        private Double totalAmount;

        public Booking(Long customerId, Long vehicleId, String serviceType,
                       String bookingDate, String bookingTime, Double totalAmount) {
            this.customerId = customerId;
            this.vehicleId = vehicleId;
            this.serviceType = serviceType;
            this.bookingDate = bookingDate;
            this.bookingTime = bookingTime;
            this.totalAmount = totalAmount;
        }
    }

