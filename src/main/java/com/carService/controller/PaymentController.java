package com.carService.controller;


import com.carService.model.Payment;
import com.carService.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

    @RestController
    @RequestMapping("/api/payments")
    @CrossOrigin(origins = "*")
    public class PaymentController {

        @Autowired
        private PaymentService paymentService;

        // POST /api/payments
        // Body: { "bookingId": 1, "amount": 150.0, "method": "CARD" }
        @PostMapping
        public Payment processPayment(@RequestBody Map<String, Object> request) {
            Long bookingId = Long.valueOf(request.get("bookingId").toString());
            Double amount  = Double.valueOf(request.get("amount").toString());
            String method  = request.get("method").toString();
            return paymentService.processPayment(bookingId, amount, method);
        }

        // GET /api/payments
        @GetMapping
        public List<Payment> getAllPayments() {
            return paymentService.getAllPayments();
        }

        // GET /api/payments/booking/1
        @GetMapping("/booking/{bookingId}")
        public List<Payment> getByBooking(@PathVariable Long bookingId) {
            return paymentService.getPaymentsByBooking(bookingId);
        }

        // GET /api/payments/status/Pending
        @GetMapping("/status/{status}")
        public List<Payment> getByStatus(@PathVariable String status) {
            return paymentService.getByStatus(status);
        }

        // DELETE /api/payments/1
        @DeleteMapping("/{id}")
        public String deletePayment(@PathVariable Long id) {
            paymentService.deletePayment(id);
            return "Payment deleted";
        }
    }

