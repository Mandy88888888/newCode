import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A Spawner object is a place where a Vehicle can spawn. Each spawner is able
 * to check if there is already a Vehicle in the spot to avoid spawning multiple
 * Vehicles on top of each other.
 * 
 * These can be shown in order to help understand and build the simulation, but
 * should be hidden when your project is complete.
 * 
 * @author Mr. Chohen
 */
public class VehicleSpawner extends Actor {
    public static final Color TRANSPARENT_RED = new Color(255, 0, 0, 128);

    // minimum distance between Vehicles when they spawn. This will be used
    // as the width of the Spawners and additional Vehicles cannot spawn until
    // the previous Vehicle clears the spawner so this is effectively the
    // minimum distance between Vehicles at the moment they spawn.
    public static final int DIST_BETWEEN_CARS = 128;

    private GreenfootImage image; // Image representing the spawner
    private int laneNumber; // Lane number this spawner represents
    private double speedModifier; // Modifies the speed of vehicles spawned
    private boolean rightward; // Direction the spawner faces (right or left)
    private boolean visible; // Determines if the spawner is visible in the
                                // world
    private int height, width; // Dimensions of the spawner

    /**
     * Constructor for the VehicleSpawner class. Initializes the spawner with its 
     * lane number, direction, and dimensions based on the provided lane height.
     * 
     * @param rightward   Indicates if the spawner faces rightward.
     * @param laneHeight  The height of the lane, used to set the spawner's height.
     * @param laneNumber  The lane number this spawner will represent.
     */
    public VehicleSpawner(boolean rightward, int laneHeight, int laneNumber) {
        this.laneNumber = laneNumber; // Set the lane number
        this.rightward = rightward; // Set the direction
        this.height = (int) (laneHeight * 0.75); // Calculate height of the spawner
        width = DIST_BETWEEN_CARS; // Set the width of the spawner
        Font laneFont = new Font("Courier New", true, false, (int) (height * 0.8)); // Font for displaying lane number

        // Visibility flag to help during development
        visible = VehicleWorld.SHOW_SPAWNERS; 
        speedModifier = 1.0; // Default speed modifier
        image = new GreenfootImage(width, height); // Create an image for the spawner

        // If the spawner is visible, fill it with red and draw the lane number
        if (visible) {
            image.setColor(TRANSPARENT_RED); // Set color for the spawner
            image.fillRect(0, 0, width - 1, height - 1); // Fill the rectangle
            image.setColor(Color.WHITE); // Set color for text
            image.setFont(laneFont); // Set the font
            image.drawString("" + laneNumber, 10, (int) (height * 0.8)); // Draw left lane number
            image.drawString("" + laneNumber, width - 28, (int) (height * 0.8)); // Draw right lane number
        }

        setImage(image); // Set the spawner image
    }
    /**
     * Checks if the spawner faces rightward.
     * 
     * @return true if the spawner faces rightward, false otherwise.
     */
    public boolean facesRightward() {
        return rightward;
    }

    /**
     * Checks if the spawner is currently touching any vehicle.
     * 
     * @return true if a vehicle is touching the spawner, false otherwise.
     */
    public boolean isTouchingVehicleOld() {
        // Legacy method; same as isTouchingVehicle
         return this.isTouching(Vehicle.class); 
    }

    /**
     * Checks if the spawner is currently touching any vehicle.
     * 
     * @return true if a vehicle is touching the spawner, false otherwise.
     */
    public boolean isTouchingVehicle() {
        // Check if the spawner is touching a vehicle
        return this.isTouching(Vehicle.class);                                    
    }

    /**
     * 
     * Get the lane number (0 indexed) that this VehicleSpawner represents. This
     * can be used by the Vehicle to figure out where it is, and when faced with
     * the lane change algorithm task, which lanes it can move into.
     * 
     * @return int The lane number in a 0-indexed format.
     */
    public int getLaneNumber() {
        return this.laneNumber; // Return the lane number
    }
}
