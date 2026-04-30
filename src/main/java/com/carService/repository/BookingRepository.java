package com.carService.repository;


import com.carService.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

    public interface BookingRepository extends JpaRepository<Booking, Long> {
        // Spring auto-generates all SQL — you just define method names!
        List<Booking> findByCustomerId(Long customerId);
        List<Booking> findByStatus(String status);
    }

