import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The BananaWolf subclass
 */
public class BananaWolf extends Vehicle
{
    public BananaWolf(VehicleSpawner origin, VehicleWorld world){
        super (origin, world); // call the superclass' constructor first
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
       
          Sheep p = (Sheep) checkTouchVehicle(direction, Sheep.class);
          if (p != null && !p.isAwake()) {
              p.healMe();
              waitFor(60);
              VehicleWorld.sheepDeadCounter --;
              return true;
          }
          return false;
    }
}