
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The SuperVehicle subclass 
 */
public class SuperVehicle extends Vehicle
{
    private boolean hit = false;
    public SuperVehicle(VehicleSpawner origin){
        super (origin); // call the superclass' constructor first
        GreenfootImage image = new GreenfootImage ("SuperVehicle.png");
        image.scale((int)(image.getWidth()*1.0/6.0), (int)(image.getHeight()*1.0/6.0));
        setImage(image);
        //Set up values for Bus
        maxSpeed = 1.5 + ((Math.random() * 10)/5); 
        speed = maxSpeed;
        // because the Bus graphic is tall, offset it a up (this may result in some collision check issues)
        yOffset = 10;
    }

    /**
     * @override
     * Act - do whatever the Bus wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
       if (hitPedestrian > 0) {
            hitPedestrian --;
            return;
        } else {
            drive(); 
        if (!checkHitPedestrian()){
            repelPedestrians();
        } else {
            hitPedestrian = counter;
        }

        if (checkEdge()){
            getWorld().removeObject(this);
            VehicleWorld.noEvent = true;
            return;
        }
    }
}

    public boolean checkHitPedestrian () {
        for (int i = -1; i <= 1; i++) {
            Sheep p = (Sheep)getOneObjectAtOffset((int)speed, i * VehicleWorld.getLaneHeight(), Sheep.class);
        if (p != null && !p.isAwake())
        {
            p.healMe();
 
        }
    }
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
