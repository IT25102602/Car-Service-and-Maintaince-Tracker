package com.carService.controller;


import com.carService.model.Booking;
import com.carService.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

    @RestController
    @RequestMapping("/api/bookings")
    @CrossOrigin(origins = "*")   // allows your HTML to call this API
    public class BookingController {

        @Autowired
        private BookingService bookingService;

        // POST /api/bookings  → create booking
        @PostMapping
        public Booking createBooking(@RequestBody Booking booking) {
            return bookingService.createBooking(booking);
        }

        // GET /api/bookings  → get all bookings
        @GetMapping
        public List<Booking> getAllBookings() {
            return bookingService.getAllBookings();
        }

        // GET /api/bookings/customer/1  → bookings for customer 1
        @GetMapping("/customer/{customerId}")
        public List<Booking> getByCustomer(@PathVariable Long customerId) {
            return bookingService.getBookingsByCustomer(customerId);
        }

        // PUT /api/bookings/3/status  → update status
        @PutMapping("/{id}/status")
        public Booking updateStatus(@PathVariable Long id, @RequestParam String status) {
            return bookingService.updateStatus(id, status);
        }

        // DELETE /api/bookings/3  → cancel booking
        @DeleteMapping("/{id}")
        public String cancelBooking(@PathVariable Long id) {
            bookingService.cancelBooking(id);
            return "Booking cancelled";
        }
    }

