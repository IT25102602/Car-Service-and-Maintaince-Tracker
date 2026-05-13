# ============================================================
#  CarService Vehicle Management – application.properties
# ============================================================

# Server
server.port=8080

# ── MySQL Database ──
spring.datasource.url=jdbc:mysql://localhost:3306/carservice_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=your_password_here
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# ── JPA / Hibernate ──
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# ── Logging ──
logging.level.com.carservice=DEBUG
logging.level.org.hibernate.SQL=DEBUG

# ── CORS (allow frontend dev server) ──
# Handled programmatically in CorsConfig.java