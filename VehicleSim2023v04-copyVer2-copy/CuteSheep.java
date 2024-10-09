import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class CuteSheep here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CuteSheep extends Sheep
{
 
    /**
     * Act - do whatever the CuteSheep wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public CuteSheep(int direction) {
          super(direction);
        maxSpeed = 2;
        this.image = new GreenfootImage("LanYangYang.png");
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
