import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Wolf class is a subclass of Vehicle that represents a wolf in the game.
 * It has the ability to 
 *     - knowck down sheeps
 *     - get hit by red wolf
 */
public class Wolf extends Vehicle {
    // Timer for tracking actions
    private int timer = 0; 
    
    // Flag to indicate if the wolf is waiting
    private boolean waiting = false; 

    /**
     * Constructor for the Wolf class.
     * Initializes the wolf's image, speed, and following distance.
     * 
     * @param origin The VehicleSpawner from which this Wolf originates.
     * @param world The VehicleWorld in which this Wolf exists.
     */
    public Wolf(VehicleSpawner origin, VehicleWorld world) {
        super(origin, world); // Call the superclass' constructor
        GreenfootImage image = new GreenfootImage("HuiTaiLang.png");
        image.scale((int) (image.getWidth() * 1.0 / 8.0), (int) (image.getHeight() * 1.0 / 8.0));
        setImage(image); // Set the scaled image

        maxSpeed = 1.5 + ((Math.random() * 30) / 5); // Randomize the maximum speed
        speed = maxSpeed; // Initialize the current speed
        yOffset = 5; // Offset for image positioning
        followingDistance = 6; // Distance to maintain from other vehicles
    }

    public void act() {
        super.act(); // Call the superclass act method
        if (getWorld() != null) { // check if wolf is already removed from world
            checkHitRedWolf(); // Check for collisions with RedWolf instances
        }
        
    }

    /**
     * Checks if the Wolf has hit a Sheep.
     * If a hit occurs, the Sheep loses blood and sound effects are played.
     * 
     * @return true if a Sheep was hit, false otherwise.
     */
    public boolean checkHitPedestrian() {
        Sheep p = (Sheep) getOneObjectAtOffset(direction * (int)speed + getImage().getWidth()/2, 0, Sheep.class);
        if (p != null && p.isAwake()) {
            if (!p.isRedWolf()) { // Ensure the sheep isn't a RedWolf
                Greenfoot.playSound("mie.mp3"); // Play sound effect
                if (!waiting) {
                    p.loseBlood(); // Cause the sheep to lose blood
                }
                waitFor(60); // Pause actions temporarily
                speed = 0; // Stop movement
                return true; // Return true indicating a hit occurred
            }
        }
        return false;
    }

    /**
     * Handles the logic for when the Wolf gets hit.
     * Plays an explosion sound, creates an animation, and removes the Wolf from the world.
     */
    public void getHit() {
        this.speed = 0; // Stop the wolf's movement
        Animation explode; // Create a new animation
        Greenfoot.playSound("Slap.mp3"); // Play explosion sound
        if (Greenfoot.getRandomNumber(2) == 1) { // Choose between one of the animations
             explode = new Animation(100, "hit.png"); // Create animation with hit.png
        }else {
             explode = new Animation(100, "RedWolf2.png"); // Create animation with RedWolf2.png
        }
        getWorld().addObject(explode, getX() + 25, getY()); // Add the animation to the world
        removeVehicle(); // Remove the wolf from the game
    }

    /**
     * Checks if the Wolf has collided with a RedWolf.
     * If a collision occurs, the Wolf is removed from the world.
     * 
     * @return true if a RedWolf was hit, false otherwise.
     */
    private boolean checkHitRedWolf() {
        RedWolf r = (RedWolf) checkTouchVehicle(direction, RedWolf.class);
        if (r != null && r.getWorld() != null) {
            getHit(); // Handle the hit logic
            return true; // Return true indicating a hit occurred
        }
        return false; // No hit occurred
    }
}