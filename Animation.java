import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Animation class is used to display an animated image on the screen for a 
 * specified duration before it is removed from the world. 
 * This class extends SuperSmoothMover to inherit movement capabilities.
 * 
 * @author Mandy Yuan
 */
public class Animation extends SuperSmoothMover {

    // The image to display for the animation
    private GreenfootImage image; 
    
    // The duration for which the animation will be displayed (in act cycles)
    private int time; 
    
    // A flag to indicate if the animation is currently waiting
    private boolean wait = true; 

    /**
     * Constructor for the Animation class. Initializes the animation with a 
     * specified duration and the image file to display.
     * 
     * @param time The number of act cycles the animation should last.
     * @param s The filename of the image to be displayed for the animation.
     */
    public Animation(int time, String s) {
        this.time = time; // Set the duration for the animation
        image = new GreenfootImage(s); // Load the image for the animation
        
        // Scale the image down to a third of its original size
        image.scale((int) (image.getWidth() * 1.0 / 3.0), (int) (image.getHeight() * 1.0 / 3.0));
    }
    public void act() {
        setImage(this.image); // Set the image for this Animation object
        
        // Check if the animation duration has expired
        if (time == 0) {
            getWorld().removeObject(this); // Remove this Animation from the world
        } else {
            time--; // Decrease the remaining time
        }

        return; // Exit the act method
    }
}
