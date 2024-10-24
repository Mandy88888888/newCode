import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The RedWolf class represents a type of sheep who comes out and hits the Wolf
 * during "red mode"
 * 
 * @author Mandy Yuan
 * @version Oct 19, 2024
 */
public class RedWolf extends Sheep {
    /**
     * Constructor for the RedWolf class. Initializes the RedWolf with a
     * specified direction and sets its properties such as speed, appearance,
     * and health.
     * 
     * @param direction
     *            The initial direction the RedWolf will move in.
     */
    public RedWolf(int direction) {
        super(direction); // Call the superclass constructor to initialize
                            // inherited properties

        isRedWolf = true; // Set a flag indicating this is a RedWolf instance,
                            // useful when wolf is knocking down Sheeps
        maxSpeed = 3; // Set the maximum speed for the RedWolf
        this.image = new GreenfootImage("RedWolf.png"); // Load the image for
                                                        // the RedWolf
                                                        
        // scale the image down
        image.scale((int) (image.getWidth() * 1.0 / 4.0), (int) (image.getHeight() * 1.0 / 4.0)); 
        setImage(image); // Set the scaled image for this RedWolf

        speed = maxSpeed; // Set the current speed to the maximum speed
        blood = 10; // Set the number of "lives" or hits the RedWolf can take
                    // before it "dies"
                    // (this is NOT very necessary because the redWolf will not
                    // be 'knocked down')
    }

    /**
     * Act - defines the actions that the RedWolf will perform during each game
     * iteration. This method is called whenever the 'Act' or 'Run' button is
     * pressed in the environment.
     */
    public void act() {
        super.act(); // Call the act method of the superclass
        if (getWorld() != null) {
            checkTouchWolf();   
        }
    }
    public void checkTouchWolf() {
        Wolf w = (Wolf) checkTouchSheep(direction, Wolf.class);
        if (w != null) {
            w.getHit();
        }
    }
}
