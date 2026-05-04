@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vehicleType;
    private String make;
    private String model;
    private int year;
    private int mileage;
    private String vin;
    private Long customerId;

    // Getters and Setters 
}