import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class SmartSheep here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SmartSheep extends Sheep
{

    /**
     * Act - do whatever the SmartSheep wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public SmartSheep(int direction) {
        super(direction);
        maxSpeed = 3;
        this.image = new GreenfootImage("XiYangYang.png");
        image.scale((int)(image.getWidth()*1.0/8.0), (int)(image.getHeight()*1.0/8.0));
        setImage(image);
        speed = maxSpeed;
        blood = 1;
 
    }
    public void act()
    {
        // Add your action code here.
        super.act();
       
    }
}
