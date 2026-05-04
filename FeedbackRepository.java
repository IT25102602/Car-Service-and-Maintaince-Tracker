package com.carservicetracker.Repository;

import com.carservicetracker.Model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByServiceId(Long serviceId);
    List<Feedback> findByVehiclePlate(String vehiclePlate);
}