package com.carservice.repository;

import com.carservice.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByCustomerId(Long customerId);

    Optional<Vehicle> findByVin(String vin);

    Optional<Vehicle> findByLicensePlate(String licensePlate);

    @Query("""
        SELECT v FROM Vehicle v
        WHERE v.customerId = :customerId
          AND (LOWER(v.make) LIKE LOWER(CONCAT('%', :q, '%'))
            OR LOWER(v.model) LIKE LOWER(CONCAT('%', :q, '%'))
            OR LOWER(v.vin) LIKE LOWER(CONCAT('%', :q, '%'))
            OR LOWER(v.licensePlate) LIKE LOWER(CONCAT('%', :q, '%')))
        """)
    List<Vehicle> searchByCustomer(@Param("customerId") Long customerId, @Param("q") String q);

    List<Vehicle> findByCustomerIdAndVehicleType(Long customerId, String vehicleType);

    List<Vehicle> findByCustomerIdAndStatus(Long customerId, String status);

    long countByCustomerId(Long customerId);
}