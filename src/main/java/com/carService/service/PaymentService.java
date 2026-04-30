package com.carService.service;


import com.carService.model.CardPayment;
import com.carService.model.CashPayment;
import com.carService.model.OnlinePayment;
import com.carService.model.Payment;
import com.carService.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

    @Service
    public class PaymentService {

        private final PaymentRepository paymentRepository;

        public PaymentService(PaymentRepository paymentRepository) {
            this.paymentRepository = paymentRepository;
        }

        public Payment processPayment(Long bookingId, Double amount, String method) {
            Payment payment;
            switch (method.toUpperCase()) {
                case "CARD":   payment = new CardPayment(bookingId, amount);   break;
                case "ONLINE": payment = new OnlinePayment(bookingId, amount); break;
                default:       payment = new CashPayment(bookingId, amount);
            }
            payment.processPayment();
            return paymentRepository.save(payment);
        }

        public List<Payment> getAllPayments() {
            return paymentRepository.findAll();
        }

        public List<Payment> getPaymentsByBooking(Long bookingId) {
            return paymentRepository.findByBookingId(bookingId);
        }

        public List<Payment> getByStatus(String status) {
            return paymentRepository.findByStatus(status);
        }

        public void deletePayment(Long id) {
            paymentRepository.deleteById(id);
        }
    }

