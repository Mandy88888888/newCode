
import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The SuperVehicle subclass represents a special type of vehicle in the game.
 * It inherits from the Vehicle class and includes unique behaviors and attributes.
 * It is spawn when there are a certain number of sheep in the world and re-spawn all the sheep
 * 
 * @author Mandy Yuan
 * @version Oct 14, 2024
 */
public class SuperVehicle extends Vehicle {
    private boolean hit = false; // Indicates if the vehicle has hit a pedestrian

    /**
     * Constructor for the SuperVehicle class. Initializes the vehicle's properties
     * and sets its image.
     * 
     * @param origin The VehicleSpawner that spawned this vehicle
     * @param world The VehicleWorld where this vehicle exists, allow access to method in VehicleWorld class
     */
    public SuperVehicle(VehicleSpawner origin, VehicleWorld world) {
        super(origin, world); // Call the superclass' constructor to initialize common properties
        
        // Load the vehicle image and scale it down
        GreenfootImage image = new GreenfootImage("SuperVehicle.png");
        image.scale((int) (image.getWidth() * 1.0 / 6.0), (int) (image.getHeight() * 1.0 / 6.0));
        setImage(image); // Set the scaled image as the vehicle's appearance
        
        // Set up the maximum speed for the SuperVehicle
        maxSpeed = 1.5 + ((Math.random() * 10) / 5); // Randomize speed between 1.5 and 3.0
        speed = maxSpeed; // Set the current speed to maxSpeed
        
        // Adjust the vertical offset due to the height of the Bus graphic
        yOffset = 10; // Offset to improve collision detection
    }

    /**
     * Act method for the SuperVehicle. This method is called whenever the 'Act'
     * or 'Run' button is pressed in the environment. It contains the main logic 
     * for the vehicle's actions each frame.
     * 
     * @Override
     */
    public void act() {
        // Check if the vehicle has hit a pedestrian
        if (hitPedestrian > 0) {
            hitPedestrian--; // Decrement hitPedestrian counter
            return; // Exit the method to avoid further actions
        } else {
            drive(); // Move the vehicle based on its speed and direction
            
            // Check for pedestrian interactions
            if (!checkHitPedestrian()) {
                repelPedestrians(); // Repel pedestrians if none were hit
            } else {
                hitPedestrian = counter; // Set the hitPedestrian counter
            }

            // Check if the vehicle has reached the edge of the world
            if (checkEdge()) {
                removeVehicle(); // Remove the vehicle from the world
                VehicleWorld.noEvent = true; // Indicate that no events should occur
                return; // Exit the method
            }
        }
    }

    /**
     * Checks if the SuperVehicle has hit a pedestrian.
     * If a pedestrian is detected and is not awake, it heals the pedestrian
     * and updates the sheepDeadCounter.
     * 
     * @return true if a pedestrian was hit; false otherwise
     */
    public boolean checkHitPedestrian() {
        Sheep p = (Sheep) checkTouchVehicle(direction, Sheep.class); // Check for a pedestrian in front
        if (p != null && !p.isAwake()) { // If a pedestrian is hit and is not awake
            waitFor(60); // Wait for 30 ticks
            p.healMe(); // Heal the pedestrian
            VehicleWorld.sheepDeadCounter--; // Decrease the dead sheep counter
            return true; // Return true indicating a hit
        }
        return false; // Return false if no hit occurred
    }
}
