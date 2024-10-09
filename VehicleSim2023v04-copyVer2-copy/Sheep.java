import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A Pedestrian that tries to walk across the street
 */
public abstract class Sheep extends SuperSmoothMover
{
    protected GreenfootImage image;
    protected double speed;
    protected double maxSpeed;
    protected int timer = 30;
    protected int direction; // direction is always -1 or 1, for moving down or up, respectively
    protected boolean awake, entering;
    protected int blood = 2;
    public Sheep(int direction) {
        // choose a random speed
       
        this.direction = direction;
    }
   // public abstract void superPower()

    /**
     * Act - do whatever the Pedestrian wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Awake is false if the Pedestrian is "knocked down"
        if (awake){
            // Check in the direction I'm moving vertically for a Vehicle -- and only move if there is no Vehicle in front of me.
            if (getOneObjectAtOffset(0, (int)(direction * getImage().getHeight()/2 + (int)(direction * speed)), Vehicle.class) == null){
                setLocation (getX(), getY() + (speed*direction));
            }
            if (direction == -1 && getY() < 325){
                getWorld().removeObject(this);
            } else if (direction == 1 && getY() > getWorld().getHeight() - 30){
                getWorld().removeObject(this);
            }

        }else {
            timer --;
            if (timer <= 0) {
                GreenfootImage image = new GreenfootImage("KaoQuanYang.png");
                image.scale((int)(image.getWidth()*1.0/10.0), (int)(image.getHeight()*1.0/10.0));
                setImage(image);
            } 
        }
    }

    /**
     * Method to cause this Pedestrian to become knocked down - stop moving, turn onto side
     */
    public void knockDown () {
        speed = 0;
        awake = false;
        VehicleWorld.sheepDeadCounter ++;
        
    }

    /**
     * Method to allow a downed Pedestrian to be healed
     */
    public void healMe () {
        speed = maxSpeed;

        setImage(image);
        awake = true;
        timer = 30;
        VehicleWorld.sheepDeadCounter --;
    }

    public boolean isAwake () {
        return awake;
    }

}
