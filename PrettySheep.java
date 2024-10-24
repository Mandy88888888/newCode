import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The PrettySheep class represents a type of sheep with a specific appearance
 * 		-comes out both day and night
 * 		-only 1 blood
 * 		-speed is 2.5
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class PrettySheep extends Sheep {

	/**
	 * Act - do whatever the PrettySheep wants to do. This method is called
	 * whenever the 'Act' or 'Run' button gets pressed in the environment.
	 */
	public PrettySheep(int direction) {
		// call the super constructer first
		super(direction);
		maxSpeed = 2.5;
		// set the maximum speed
		speed = maxSpeed;
		// set the speed to the maximum speed because sheep is moving
		this.image = new GreenfootImage("MeiYangYang.png");
		image.scale((int) (image.getWidth() * 1.0 / 8.0), (int) (image.getHeight() * 1.0 / 8.0));
		// change the dimension
		setImage(image);
		// set the image
		blood = 1;
		// how many times the sheep can "die"

	}

	public void act() {
		super.act();
	}
}
