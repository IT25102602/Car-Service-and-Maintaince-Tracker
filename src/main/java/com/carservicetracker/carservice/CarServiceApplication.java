package com.carservicetracker.carservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.SpringVersion;

@SpringBootApplication(scanBasePackages = "com.carservicetracker")
public class CarServiceApplication {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Car Service Tracker Application   ");
        System.out.println("   Spring Version: " + SpringVersion.getVersion());
        System.out.println("=====================================");

        SpringApplication.run(CarServiceApplication.class, args);
    }
}