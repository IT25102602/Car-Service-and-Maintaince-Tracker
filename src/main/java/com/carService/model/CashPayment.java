package com.carService.model;

import jakarta.persistence.*;

    @Entity
    @DiscriminatorValue("CASH")
    public class CashPayment extends Payment {

        public CashPayment() {}

        public CashPayment(Long bookingId, Double amount) {
            super(bookingId, amount);
        }

        @Override
        public String processPayment() {
            this.setStatus("Paid");
            this.setTransactionId("CASH-" + System.currentTimeMillis());
            return "Cash payment of $" + getAmount() + " recorded.";
        }

        @Override
        public String getPaymentMethod() { return "Cash"; }
    }

