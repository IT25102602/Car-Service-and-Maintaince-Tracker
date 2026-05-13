CREATE DATABASE IF NOT EXISTS carservice_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE carservice_db;

CREATE TABLE IF NOT EXISTS vehicles (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    vehicle_category    VARCHAR(20)     DEFAULT 'VEHICLE',
    vehicle_type        VARCHAR(30)     NOT NULL,
    make                VARCHAR(50)     NOT NULL,
    model               VARCHAR(50)     NOT NULL,
    year                INT             NOT NULL,
    mileage             INT             NOT NULL DEFAULT 0,
    vin                 VARCHAR(17),
    license_plate       VARCHAR(20),
    owner_name          VARCHAR(100),
    status              VARCHAR(20)     DEFAULT 'Active',
    next_service        VARCHAR(100),
    next_service_km     INT,
    next_service_days   INT,
    service_progress    INT             DEFAULT 20,
    num_doors           INT,
    fuel_type           VARCHAR(20),
    engine_cc           INT,
    bike_type           VARCHAR(30),
    customer_id         BIGINT,
    created_at          DATETIME        DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS customers (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(100),
    phone       VARCHAR(20),
    created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT IGNORE INTO customers (id, name, email, phone)
VALUES
    (1, 'Kaviska Pathum', 'kaviska@email.com', '+94 71 234 5678'),
    (2, 'Pathum Silva',   'pathum@email.com',  '+94 77 345 6789');