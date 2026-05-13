package com.carservice.repository;

import com.carservice.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository layer – OOP: Encapsulation (data access hidden behind interface)
 * CRUD: Read operations
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // ── READ: Search by customer ──
    List<Vehicle> findByCustomerId(Long customerId);

    // ── READ: Search by VIN ──
    Optional<Vehicle> findByVin(String vin);

    // ── READ: Search by license plate ──
    Optional<Vehicle> findByLicensePlate(String licensePlate);

    // ── READ: Full text search (make, model, VIN, plate) ──
    @Query("""
        SELECT v FROM Vehicle v
        WHERE v.customerId = :customerId
          AND (LOWER(v.make) LIKE LOWER(CONCAT('%', :q, '%'))
            OR LOWER(v.model) LIKE LOWER(CONCAT('%', :q, '%'))
            OR LOWER(v.vin) LIKE LOWER(CONCAT('%', :q, '%'))
            OR LOWER(v.licensePlate) LIKE LOWER(CONCAT('%', :q, '%')))
        """)
    List<Vehicle> searchByCustomer(@Param("customerId") Long customerId, @Param("q") String q);

    // ── READ: Filter by type ──
    List<Vehicle> findByCustomerIdAndVehicleType(Long customerId, String vehicleType);

    // ── READ: Filter by status ──
    List<Vehicle> findByCustomerIdAndStatus(Long customerId, String status);

    // ── READ: Count by customer ──
    long countByCustomerId(Long customerId);

    // ── READ: Find due vehicles ──
    @Query("SELECT v FROM Vehicle v WHERE v.customerId = :cid AND v.status = 'Service Due'")
    List<Vehicle> findServiceDueByCustomer(@Param("cid") Long customerId);
}