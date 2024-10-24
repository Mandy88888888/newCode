import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 *  The CuteSheep class represents a specific type of Sheep that is
 *  	- slower (speed = 2)
 *  	- comes out only during the day
 *  	- has an extra life 
 * 
 * @author Mandy Yuan
 */
public class CuteSheep extends Sheep {

	/**
	 * Act - do whatever the CuteSheep wants to do. This method is called
	 * whenever the 'Act' or 'Run' button gets pressed in the environment.
	 */
	public CuteSheep(int direction) {
		super(direction); // Call the superclass constructor to initialize inherited properties
		
		maxSpeed = 2; // Set the maximum speed (the CuteSheep is slower)
        speed = maxSpeed; // Set the current speed to maximum speed
		
		// Load and scale the image for CuteSheep
        this.image = new GreenfootImage("LanYangYang.png");
        image.scale((int) (image.getWidth() * 1.0 / 8.0), (int) (image.getHeight() * 1.0 / 8.0)); // Scale down the image
        setImage(image); // Set the scaled image for this CuteSheep
        
		blood = 2; // this sheep get extra blood for being my favourite!!
	}

	public void act() {
		// Add your action code here.
		super.act();
	}

	@Override
	public void healMe() {

		// sheep run at max speed when healed
		speed = maxSpeed;
		// set the image to original image
		setImage(image);
		// change the awake status to true again
		awake = true;
		// one less "dead sheep" in the world
		VehicleWorld.sheepDeadCounter--;
		// Extra blood
		blood = 2;

	}
}
