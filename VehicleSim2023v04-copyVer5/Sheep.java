import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
import java.util.ArrayList;

/**
 * A Pedestrian that tries to walk across the street
 */
public abstract class Sheep extends SuperSmoothMover
{
    protected GreenfootImage image;//
    protected double speed; // the current speed of the sheep
    protected double maxSpeed; //the maximum speed of the sheep
    protected int timer = 30; //timer for when the sheep is waiting
    protected int direction; // direction is always -1 or 1, for moving down or up, respectively
    protected boolean awake, entering; //traits that shows status of the sheep
    protected int blood;
    public static boolean avoidingWolf = false;
    protected boolean avoided = false;
    public Sheep(int direction) {
        // this is the direction I am moving in
        this.direction = direction;
        entering = true;
        //this is a new sheep, so ythe world won't think I reach the edge alread
        awake = true; //this sheep is awake
        
    }
   // public abstract void superPower() 
   //TODO: Coming soon

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
            //if I am avoiding the wolf, use the avoid wolf method
            if (avoidingWolf) {
                avoidWolf();
            }
            //check if I reached the edge of the world - if I did, remove me
            if (direction == -1 && getY() < 325){
                getWorld().removeObject(this);
            } else if (direction == 1 && getY() > getWorld().getHeight() - 30){
                getWorld().removeObject(this);
            }

        }else {
            //if I am not awake but I am still counting down to wait to be knocked out
            timer --;
            if (timer <= 0) {
                //when the timer reaches '0', I am knocked out and my picture changes
                GreenfootImage image = new GreenfootImage("KaoQuanYang.png");
                image.scale((int)(image.getWidth()*1.0/10.0), (int)(image.getHeight()*1.0/10.0));
                setImage(image);
            } 
        }
    }
    public void avoidWolf() {
        if (!avoided) {
            Wolf w;
            w = (Wolf) getOneObjectAtOffset(getX(), direction * (int) (speed + getImage().getHeight()/2), Wolf.class);
            if (w != null) {
                sheepChangeDirection();
                avoided = true;
            }
            
        }
        
    }
    private void sheepChangeDirection() {
        if (direction == -1) {
            direction = 1;
        } else {
            direction = -1;
        }
    }

    /**
     * Method to cause sheep to lose blood with one attack
     */
    public void loseBlood () {
        blood-=1;
        // knock down the sheep if it has no more blood left
        if (blood <= 0) {
            knockDown();
        }
        else {
            Animation looseBlood = new Animation (50, "minus.png");
            getWorld().addObject(looseBlood, getX() + 25, getY());
        }
    }
    /**
     * Method to "killed" a sheep (stop moving and become food)
     */
    public void knockDown () {
        // no longer moving
        speed = 0;
        //change the status to not awake
        awake = false;
        //add one "dead sheep" to the world
        VehicleWorld.sheepDeadCounter ++;
        
    }

    /**
     * Method to allow a "killed" sheep to be healed
     */
    public void healMe () {
        //sheep run at max speed when healed
        speed = maxSpeed;
        //set the image to original image
        setImage(image);
        //change the awake status to true again
        awake = true;
        //one less "dead sheep" in the world
        VehicleWorld.sheepDeadCounter --;
        //give one blood to sheep
        blood = 1;
    
    }    
    /**
     * Method that checks if the sheep is awake
     */public boolean isAwake () {
        return awake;
    }

}
