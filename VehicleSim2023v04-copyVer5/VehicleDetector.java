import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class VehicleDetector here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class VehicleDetector extends Actor
{
    private boolean detected = false; // there is no vehicles detected on the vehicle detector
    private Vehicle vehicle;
    private GreenfootImage image;
    private boolean upper; // if upper = true, it means this is the upper detector, else is the lower detector
    /**
     * Act - do whatever the VehicleDetector wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public VehicleDetector (Vehicle v,boolean upper) {
        vehicle = v;
        this.upper = upper;
        image =  new GreenfootImage(v.getImage().getWidth(), v.getImage().getHeight());
        image.setColor(new Color(32,88,88));
        //image = new GreenfootImage ("MeiYangYang.png");
        
    }
    
    public void act()
    {
        detect();
        updateLocation();
    }
    private void detect() {
         Vehicle p = (Vehicle)getOneObjectAtOffset((int)getImage().getWidth()/2, 0, Vehicle.class);
        if (p != null) {
            detected = true;
        } else {
            detected = false;
        }
    }
    public boolean isDetected () {
        return detected;
    }
    public void updateLocation () {
        if (vehicle == null) {
            //if the vehicle doesn't exist already, remove 'me' from the world
            getWorld().removeObject(this);
        } else {
          if (upper) {
            setLocation (vehicle.getX(), vehicle.getY() - VehicleWorld.getLaneHeight());
            } else {
            setLocation (vehicle.getX(), vehicle.getY() + VehicleWorld.getLaneHeight());
            }  
        }
        
    }
}
