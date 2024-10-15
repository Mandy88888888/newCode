import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The LittleWolf subclass
 */
public class LittleWolf extends Vehicle
{
    private boolean hit = false;
    public LittleWolf(VehicleSpawner origin){
        super (origin); // call the superclass' constructor first
        GreenfootImage image = new GreenfootImage ("XiaoHuiHui.png");
        image.scale((int)(image.getWidth()*1.0/8.0), (int)(image.getHeight()*1.0/8.0));
        setImage(image);
        
        //Set up values for LittleWolf
        maxSpeed = 1.5 + ((Math.random() * 10)/5);
        speed = maxSpeed;
    }

    /**
     * Act - do whatever the LittleWolf wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
       super.act();
    }

    public boolean checkHitPedestrian () {
        
               for (int i = -1; i <= 1; i++) {
            Sheep p = (Sheep)getOneObjectAtOffset((int)speed, i * (VehicleWorld.getLaneHeight()), Sheep.class);
        
        if (p != null && !p.isAwake())
        {
            //p.sleepFor(30);
            getWorld().removeObject(p);
        Greenfoot.playSound("eat.mp3");
        VehicleWorld.sheepDeadCounter --;
        return true;
        
 
        }}
         Sheep p = (Sheep)getOneObjectAtOffset((int)speed, -(VehicleWorld.getLaneHeight() + 6), Sheep.class);
        if (p != null && !p.isAwake())
        {
            getWorld().removeObject(p);
        Greenfoot.playSound("eat.mp3");
        VehicleWorld.sheepDeadCounter --;
        return true;
 
        }
         p = (Sheep)getOneObjectAtOffset((int)speed, (VehicleWorld.getLaneHeight() + 6), Sheep.class);
        if (p != null && !p.isAwake())
        {
            getWorld().removeObject(p);
        Greenfoot.playSound("eat.mp3");
        VehicleWorld.sheepDeadCounter --;
        return true;
 
        }
        return false;
    }
 
}
