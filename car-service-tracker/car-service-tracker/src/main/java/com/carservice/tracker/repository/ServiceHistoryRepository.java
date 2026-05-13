package com.carservice.tracker.repository;

import com.carservice.tracker.model.ServiceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceHistoryRepository extends JpaRepository<ServiceHistory, String> {
    List<ServiceHistory> findByVehicleNumberContaining(String vehicleNumber);
    List<ServiceHistory> findByCustomerNameContaining(String customerName);
}
