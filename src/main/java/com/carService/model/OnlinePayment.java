package com.carService.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ONLINE")
public class OnlinePayment extends Payment {

    public OnlinePayment() {}

    public OnlinePayment(Long bookingId, Double amount) {
        super(bookingId, amount);
    }

    @Override
    public String processPayment() {
        this.setStatus("Paid");
        this.setTransactionId("ONLINE-" + System.currentTimeMillis());
        return "Online payment of $" + getAmount() + " completed via gateway.";
    }

    @Override
    public String getPaymentMethod() { return "Online"; }
}