package com.carservice.service;

import com.carservice.model.Car;
import com.carservice.model.Motorcycle;
import com.carservice.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * DataLoader – seeds dummy vehicle data on first startup
 * Runs only if the vehicles table is empty
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (vehicleRepository.count() == 0) {
            seedDemoData();
            System.out.println("✅ Demo vehicle data seeded successfully!");
        } else {
            System.out.println("ℹ️  Vehicle data already exists – skipping seed.");
        }
    }

    private void seedDemoData() {

        /* ── Vehicle 1: Toyota Camry (Car) ── */
        Car camry = new Car();
        camry.setVehicleType("Car");
        camry.setMake("Toyota");
        camry.setModel("Camry");
        camry.setYear(2020);
        camry.setMileage(45000);
        camry.setVin("4T1C11AK4LU123456");
        camry.setLicensePlate("ABC-1234");
        camry.setOwnerName("Kaviska Pathum");
        camry.setStatus("Active");
        camry.setNextService("Oil Change & Filter");
        camry.setNextServiceKm(2000);
        camry.setNextServiceDays(45);
        camry.setServiceProgress(70);
        camry.setCustomerId(1L);
        camry.setFuelType("Petrol");
        camry.setNumDoors(4);
        vehicleRepository.save(camry);

        /* ── Vehicle 2: Honda CBR 500R (Motorcycle) ── */
        Motorcycle cbr = new Motorcycle();
        cbr.setVehicleType("Motorcycle");
        cbr.setMake("Honda");
        cbr.setModel("CBR 500R");
        cbr.setYear(2021);
        cbr.setMileage(18500);
        cbr.setVin("MH1PC5600MK123789");
        cbr.setLicensePlate("XYZ-5678");
        cbr.setOwnerName("Kaviska Pathum");
        cbr.setStatus("Service Due");
        cbr.setNextService("Chain Lubrication");
        cbr.setNextServiceKm(500);
        cbr.setNextServiceDays(10);
        cbr.setServiceProgress(90);
        cbr.setCustomerId(1L);
        cbr.setEngineCc(471);
        cbr.setBikeType("Sport");
        vehicleRepository.save(cbr);

        /* ── Vehicle 3: Nissan X-Trail (Car) ── */
        Car xtrail = new Car();
        xtrail.setVehicleType("Car");
        xtrail.setMake("Nissan");
        xtrail.setModel("X-Trail");
        xtrail.setYear(2019);
        xtrail.setMileage(62000);
        xtrail.setVin("JN1BBNT32Z0123456");
        xtrail.setLicensePlate("PQR-9012");
        xtrail.setOwnerName("Pathum Silva");
        xtrail.setStatus("Active");
        xtrail.setNextService("Full Service");
        xtrail.setNextServiceKm(3000);
        xtrail.setNextServiceDays(60);
        xtrail.setServiceProgress(55);
        xtrail.setCustomerId(2L);
        xtrail.setFuelType("Diesel");
        xtrail.setNumDoors(5);
        vehicleRepository.save(xtrail);

        /* ── Vehicle 4: Yamaha R15 (Motorcycle) ── */
        Motorcycle r15 = new Motorcycle();
        r15.setVehicleType("Motorcycle");
        r15.setMake("Yamaha");
        r15.setModel("R15 V4");
        r15.setYear(2022);
        r15.setMileage(8200);
        r15.setVin("ME1RG0618N0012345");
        r15.setLicensePlate("LMN-3456");
        r15.setOwnerName("Kaviska Pathum");
        r15.setStatus("Active");
        r15.setNextService("Oil & Air Filter");
        r15.setNextServiceKm(4800);
        r15.setNextServiceDays(90);
        r15.setServiceProgress(30);
        r15.setCustomerId(1L);
        r15.setEngineCc(155);
        r15.setBikeType("Sport");
        vehicleRepository.save(r15);
    }
}