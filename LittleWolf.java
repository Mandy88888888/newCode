import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The LittleWolf subclass
 * "Eats" the sheeps that are kncoked out (remove them from the world)
 * Resembles the 'Bus'
 */
public class LittleWolf extends Vehicle {
    
     /**
     * Constructor for the LittleWolf class. Initializes the LittleWolf with
     * an origin spawner and a reference to the game world.
     * 
     * @param origin The VehicleSpawner where this LittleWolf spawns.
     * @param world  The VehicleWorld instance this LittleWolf belongs to.
     */
    public LittleWolf(VehicleSpawner origin, VehicleWorld world) {
        super(origin, world); // call the superclass' constructor first
        
        GreenfootImage image = new GreenfootImage("XiaoHuiHui.png"); // Load the image for the LittleWolf
        image.scale((int) (image.getWidth() * 1.0 / 8.0), (int) (image.getHeight() * 1.0 / 8.0)); // Scale down the image
        setImage(image); // Set the scaled image for this LittleWolf

         // Set up values for LittleWolf
        maxSpeed = 1.5 + ((Math.random() * 10) / 5); // Randomly assign maximum speed
        speed = maxSpeed; // Set current speed to maximum speed
    }

    /**
     * Act - do whatever the LittleWolf wants to do. This method is called
     * whenever the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() {
        super.act(); // Call the act method of the superclass
    }

     /**
     * Check if the LittleWolf has hit a pedestrian (Sheep). If a hit occurs,
     * remove the pedestrian and play a sound.
     * 
     * @return true if a pedestrian was hit, false otherwise.
     */
    public boolean checkHitPedestrian() {
        Sheep p = (Sheep) checkTouchVehicle(direction, Sheep.class); // Check for collision with Sheep
        if (p != null && !p.isAwake()) { // If a Sheep is detected and it is not awake
            waitFor(20); // Wait for a specified time (30 frames)
            getWorld().removeObject(p); // Remove the Sheep from the world
            Greenfoot.playSound("eat.mp3"); // Play a sound effect for the action
            VehicleWorld.sheepDeadCounter--; // Decrement the count of dead sheep
            return true; // Indicate that a hit occurred
        }
        return false; // No hit occurred
    }

}
