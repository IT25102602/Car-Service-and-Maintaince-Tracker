package com.carService.service;

import com.carService.model.Booking;
import com.carService.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

    @Service
    public class BookingService {

        @Autowired
        private BookingRepository bookingRepository;

        // CREATE
        public Booking createBooking(Booking booking) {
            booking.setStatus("Pending");
            return bookingRepository.save(booking);
        }

        // READ
        public List<Booking> getAllBookings() {
            return bookingRepository.findAll();
        }

        public List<Booking> getBookingsByCustomer(Long customerId) {
            return bookingRepository.findByCustomerId(customerId);
        }

        // UPDATE status
        public Booking updateStatus(Long id, String newStatus) {
            Booking booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));
            booking.setStatus(newStatus);
            return bookingRepository.save(booking);
        }

        // DELETE
        public void cancelBooking(Long id) {
            bookingRepository.deleteById(id);
        }
    }

