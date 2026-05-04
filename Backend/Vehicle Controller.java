@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*") 
public class VehicleController {

    @Autowired
    private VehicleRepository vehicleRepository;

    @PostMapping
    public ResponseEntity<?> addVehicle(@RequestBody Vehicle vehicle) {
        try {
            Vehicle savedVehicle = vehicleRepository.save(vehicle);
            return new ResponseEntity<>(savedVehicle, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error saving vehicle: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}