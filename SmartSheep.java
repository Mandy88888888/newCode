import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The SmartSheep class represents a type of sheep that can move intelligently 
 * within the simulation. It inherits from the Sheep class and can have 
 * additional behaviors or properties specific to smart sheep.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */

public class SmartSheep extends Sheep {

	/**
     * Constructor for the SmartSheep class. Initializes the sheep's direction,
     * maximum speed, image, and health (blood).
     * 
     * @param direction The initial direction for the sheep to move.
     */
	public SmartSheep(int direction) {
        super(direction); // Call the superclass (Sheep) constructor with direction
        maxSpeed = 3; // Set the maximum speed for the SmartSheep

        // Create an image for the SmartSheep and scale it down
        this.image = new GreenfootImage("XiYangYang.png");
        image.scale((int) (image.getWidth() * 1.0 / 8.0), (int) (image.getHeight() * 1.0 / 8.0));
        setImage(image); // Set the scaled image for this sheep

        speed = maxSpeed; // Set the current speed to the maximum speed
        blood = 1; // Set the health (blood) of the SmartSheep
    }

	 /**
     * Act method for the SmartSheep. This method is called whenever the 
     * 'Act' or 'Run' button is pressed in the environment. It allows 
     * the SmartSheep to perform actions each turn.
     */
    public void act() {
        super.act(); // Call the act method from the Sheep class to inherit its behaviors
    }
}
