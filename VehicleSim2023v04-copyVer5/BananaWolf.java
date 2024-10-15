import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The BananaWolf subclass
 */
public class BananaWolf extends Vehicle
{
    public BananaWolf(VehicleSpawner origin){
        super (origin); // call the superclass' constructor first
        GreenfootImage image = new GreenfootImage ("JiaoTaiLang.png");
        //set the image
        image.scale((int)(image.getWidth()*1.0/8.0), (int)(image.getHeight()*1.0/8.0));
        setImage(image);
        maxSpeed = 2.5;
        speed = maxSpeed;
        yOffset = 5;
    }

    /**
     * Act - do whatever the BananaWolf wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        super.act();
        
    }

    public boolean checkHitPedestrian () {
       
               for (int i = -1; i <= 1; i++) {
            Sheep p = (Sheep)getOneObjectAtOffset((int)speed, i * VehicleWorld.getLaneHeight(), Sheep.class);
        if (p != null && !p.isAwake())
        {
            //p.sleepFor(30);
        p.healMe();
        return true;
 
        }}
         Sheep p = (Sheep)getOneObjectAtOffset((int)speed, -(VehicleWorld.getLaneHeight() + 6), Sheep.class);
        if (p != null && !p.isAwake())
        {
            p.healMe();
        return true;
 
        }
         p = (Sheep)getOneObjectAtOffset((int)speed, (VehicleWorld.getLaneHeight() + 6), Sheep.class);
        if (p != null && !p.isAwake())
        {
            p.healMe();
        return true;
 
        }
        return false;
    }
}