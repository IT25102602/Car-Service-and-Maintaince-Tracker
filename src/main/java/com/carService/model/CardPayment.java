package com.carService.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("CARD")
public class CardPayment extends Payment {

    public CardPayment() {}

    public CardPayment(Long bookingId, Double amount) {
        super(bookingId, amount);
    }

    @Override
    public String processPayment() {
        this.setStatus("Paid");
        this.setTransactionId("CARD-" + System.currentTimeMillis());
        return "Card payment of $" + getAmount() + " processed successfully.";
    }

    @Override
    public String getPaymentMethod() { return "Card"; }
}

