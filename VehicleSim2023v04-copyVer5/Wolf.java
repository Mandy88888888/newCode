import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Car subclass
 */
public class Wolf extends Vehicle
{
    private int timer = 0;
    private boolean eating = false;
    
    public Wolf(VehicleSpawner origin) {
        
        super(origin); // call the superclass' constructor
        GreenfootImage image = new GreenfootImage ("HuiTaiLang.png");
        image.scale((int)(image.getWidth()*1.0/8.0), (int)(image.getHeight()*1.0/8.0));
        setImage(image);
        maxSpeed = 1.5 + ((Math.random() * 30)/5);
        speed = maxSpeed;
        yOffset = 5;
        int z;
        followingDistance = 6;
    }

    public void act()
    {
        super.act();
        if (eating) {
            timer --;
            if (timer <= 0) {
                speed = maxSpeed;
                eating = false;
            }
                
        }

    }
 
    /**
     * When a Car hit's a Pedestrian, it should knock it over
     */

    public boolean checkHitPedestrian () {
        Sheep p = (Sheep)getOneObjectAtOffset((int)speed + getImage().getWidth()/2, 0, Sheep.class);
        if (p != null && p.isAwake())
        {
            
            Greenfoot.playSound("mie.mp3");
        
            if (!eating) {
                p.loseBlood();
            }
            
             eating = true;
             timer = 30;
             speed = 0;

            return true;
        }
        return false;
        
    }
}
