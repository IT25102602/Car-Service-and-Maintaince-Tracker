package com.carService.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

    @Entity
    @Table(name = "payments")
    @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
    @DiscriminatorColumn(name = "payment_type")
    @Getter @Setter
    public abstract class Payment {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private Long bookingId;
        private Double amount;
        private String status = "Pending";
        private String transactionId;

        public Payment() {}

        public Payment(Long bookingId, Double amount) {
            this.bookingId = bookingId;
            this.amount = amount;
        }

        // Abstract methods — each subclass MUST implement these
        public abstract String processPayment();
        public abstract String getPaymentMethod();
    }

