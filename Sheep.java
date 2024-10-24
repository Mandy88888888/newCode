import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
import java.util.ArrayList;

/**
 * Abstract class representing a Sheep that tries to walk across the street.
 * 
 * This class defines the common properties and behaviors of all sheep,
 * including movement, collision detection, and health management.
 */
public abstract class Sheep extends SuperSmoothMover {
    protected boolean isRedWolf = false; // Flag to indicate if this is a
                                            // RedWolf
    protected GreenfootImage image; // Image representing the sheep
    protected GreenfootImage foodImage; // Image when sheep turns into food
    protected double speed; // Current speed of the sheep
    protected double maxSpeed; // Maximum speed of the sheep
    protected int timer = 30; // Timer for waiting
    protected int direction; // Direction of movement (-1 for down, 1 for up)
    protected boolean awake, entering; // Status flags for the sheep
    protected int blood; // Health of the sheep

    /**
     * Constructor for the Sheep class. Initializes the direction and sets the
     * sheep as entering the world.
     * 
     * @param direction
     *            Direction in which the sheep will move.
     */
    public Sheep(int direction) {
        // this is the direction I am moving in
        this.direction = direction;
        entering = true;
        // this is a new sheep, so ythe world won't think I reach the edge
        // alread
        awake = true; // this sheep is awake
        foodImage = turnToFood();

    }

    public void act() {
        if (awake) {
            // Check for vehicles ahead and move if clear
            if (getOneObjectAtOffset(0, (int) (direction * getImage().getHeight() / 2 + (int) (direction * speed)),
                    Vehicle.class) == null) {
                setLocation(getX(), getY() + (speed * direction));
            }
            // Avoid other sheep
            avoidSheep();
            // Remove sheep if they reach the edge of the world
            if ((direction == -1 && getY() < 325) || (direction == 1 && getY() > getWorld().getHeight() - 30)) {
                getWorld().removeObject(this);
            }
        } else {
            // Countdown timer for knocked-out sheep
            timer--;
            if (timer <= 0) {
                // Change image when the timer reaches zero
                setImage(foodImage);
            }
        }
    }
    
    /** 
     * This method generates a random image for the sheep when it
     * gets "knocked out"
     */
    public GreenfootImage turnToFood(){
        GreenfootImage image = new GreenfootImage("LambChop.png"); // inicialize the image
        int n = (int) Math.round(Math.random()*2) +1; // pick a number between 1 and 3
        switch (n) {
            case 1:
                image = new GreenfootImage("LambChop.png");
                image.scale((int) (image.getWidth() * 1.0 / 5.0), (int) (image.getHeight() * 1.0 / 5.0));  
                break;
            case 2:
                image = new GreenfootImage("ShabuMutton.png");
                image.scale((int) (image.getWidth() * 1.0 / 5.0), (int) (image.getHeight() * 1.0 / 5.0));  
                break; 
            case 3:
                image = new GreenfootImage("RoastedLamb.png");
                image.scale((int) (image.getWidth() * 1.0 / 10.0), (int) (image.getHeight() * 1.0 / 10.0));  
                break;
        }
       
        return image;
    }
    /**
     * Getter method for direction
     * 
     * @return -1 or 1 that represent the direction
     */
    public int getDirection() {
        return direction;
    }
    
    /**
     * Checks if this sheep is a RedWolf.
     * The getter methof for isRedWolf
     * 
     * @return true if this sheep is a RedWolf, false otherwise.
     */
    public boolean isRedWolf() {
        return isRedWolf;
    }

    /**
     * Avoids other sheep when moving.
     * The sheep will move slightly to avoid collision if another sheep is detected.
     */
    public void avoidSheep() {
        int avoidingFactor = direction * 5;
        Sheep p = (Sheep) checkTouchSheep(direction, Sheep.class);
        if (p != null && p.isAwake()) {
            if (p.getDirection() != direction) {
                setLocation(getX() + avoidingFactor, getY());
            } 
            
        }
    }

    /**
     * Checks for collisions with other sheep in the specified direction.
     * 
     * @param direction Direction to check for collisions.
     * @param clss Class type of the object to check for.
     * @return The collided object, or null if none was found.
     */
    protected Object checkTouchSheep(int direction, Class<?> clss) {
        int location = image.getWidth() / 2;
        for (int i = -location; i <= location; i += 4) {
            Object p = (Object) getOneObjectAtOffset(i, direction * (int) speed, clss);
            if (p != null) {
                return p; // Return the collided object
            }
        }
        return null; // No collision detected
    }


    /**
     * Causes the sheep to lose one unit of blood.
     * If blood reaches zero, the sheep is knocked down.
     */
    public void loseBlood() {
        blood -= 1;
        if (blood <= 0) {
            knockDown(); // Knock down the sheep if no blood is left
        } else {
            Animation looseBlood = new Animation(50, "minus.png");
            getWorld().addObject(looseBlood, getX() + 25, getY());
        }
    }

    /**
     * Knocks down the sheep, stopping its movement and marking it as not awake.
     * Increments the dead sheep counter in the world.
     */
    public void knockDown() {
        speed = 0; // Stop movement
        awake = false; // Mark as not awake
        VehicleWorld.sheepDeadCounter++; // Increment the dead sheep counter
    }

    /**
     * Heals the sheep, allowing it to move at maximum speed again.
     * Resets the image and status, and increments blood count.
     */
    public void healMe() {
        Greenfoot.playSound("revise.mp3");
        speed = maxSpeed; // Set speed to maximum
        setImage(image); // Reset the image
        awake = true; // Mark as awake again
        VehicleWorld.sheepDeadCounter--; // Decrement the dead sheep counter
        blood = 1; // Reset blood count
    }

    /**
     * Checks if the sheep is awake.
     * Getter method for awake
     * 
     * @return true if the sheep is awake, false otherwise.
     */
    public boolean isAwake() {
        return awake;
    }

}
