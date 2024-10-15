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
        //set the maximum speed (the lazy sheep is a little bit slower)
        maxSpeed = 2;
        speed = maxSpeed;
        //set the speed to the maximum speed because sheep is moving
        this.image = new GreenfootImage("LanYangYang.png");
        image.scale((int)(image.getWidth()*1.0/8.0), (int)(image.getHeight()*1.0/8.0));
        setImage(image);
        blood = 2; //this sheep get extra blood for being my favourite!!
    }
    public void act()
    {
        // Add your action code here.
        super.act();
    }
    @Override
    public void healMe() {
        
        //sheep run at max speed when healed
        speed = maxSpeed;
        //set the image to original image
        setImage(image);
        //change the awake status to true again
        awake = true;
        //one less "dead sheep" in the world
        VehicleWorld.sheepDeadCounter --;

    }
}
