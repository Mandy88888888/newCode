import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The VehicleDetector class detects vehicles in the vicinity of a given vehicle.
 * It is used to determine if there are other vehicles in the detector's path,
 * helping to manage vehicle movements and interactions.
 * 
 * @author Mandy Yuan
 * @version Oct 17, 2024
 */
public class VehicleDetector extends Actor {
    private boolean detected = false; // Indicates whether a vehicle is detected
    private Vehicle vehicle; // The vehicle associated with this detector
    private GreenfootImage image; // The image representation of the detector
    private boolean upper; // True if this is the upper detector; false if lower
    /**
     * Constructor for the VehicleDetector class.
     * Initializes the detector with a reference to a Vehicle and its position
     * (upper or lower relative to the vehicle).
     * 
     * @param v The Vehicle that this detector is associated with
     * @param upper True if this is the upper detector, false if lower
     */
    public VehicleDetector(Vehicle v, boolean upper) {
        vehicle = v; // Set the associated vehicle
        this.upper = upper; // Determine if this is the upper or lower detector
        
     // Create an invisible image with the same dimensions as the vehicle
        image = new GreenfootImage(v.getImage().getWidth(), v.getImage().getHeight());
        setImage(image);

    }
    
    /**
     * Act method for the VehicleDetector. This method is called whenever the 
     * 'Act' or 'Run' button is pressed in the environment. It handles detection 
     * and updates the detector's location.
     */
    public void act() {
        detect(); // Check for nearby vehicles
        updateLocation(); // Update the position of the detector based on the vehicle
    }

    /**
     * Detects if a SuperSmoothMover (another vehicle) is present in front of the detector.
     * Updates the detected status based on whether another vehicle is found.
     */
    private void detect() {
        // Check for a SuperSmoothMover object at the detector's position
        SuperSmoothMover p = (SuperSmoothMover) getOneObjectAtOffset((int) getImage().getWidth() / 2, 0,
                SuperSmoothMover.class);
        if (p != null) {
            detected = true; // Vehicle detected
        } else {
            detected = false; // No vehicle detected
        }
    }

    /**
     * Returns the detection status of the detector.
     * 
     * @return True if a vehicle is detected; false otherwise
     */
    public boolean isDetected() {
        return detected; // Return the detection status
    }

    /**
     * Updates the location of the detector based on the associated vehicle's position.
     * If the vehicle no longer exists, removes the detector from the world.
     */
    public void updateLocation() {
        if (vehicle == null) {
            // If the associated vehicle is null, remove this detector from the world
            getWorld().removeObject(this);
        } else {
            // Set the detector's location relative to the vehicle
            if (upper) {
                setLocation(vehicle.getX(), vehicle.getY() - VehicleWorld.getLaneHeight()); // Upper detector
            } else {
                setLocation(vehicle.getX(), vehicle.getY() + VehicleWorld.getLaneHeight()); // Lower detector
            }
        }
    }
}
