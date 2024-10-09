import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class PrettySheep here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PrettySheep extends Sheep
{

    /**
     * Act - do whatever the PrettySheep wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public PrettySheep(int direction) {
          super(direction);
        maxSpeed = 2.5;
        this.image = new GreenfootImage("MeiYangYang.png");
        image.scale((int)(image.getWidth()*1.0/8.0), (int)(image.getHeight()*1.0/8.0));
        setImage(image);
        speed = maxSpeed;
        awake = true;
        entering = true;
    }
    public void act()
    {
        // Add your action code here.
        super.act();
    }
}
