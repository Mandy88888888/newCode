import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Animation here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Animation extends SuperSmoothMover
{
    /**
     * Act - do whatever the Animation wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private GreenfootImage image;
    private int time;
    private boolean wait = true;
    
    public Animation (int time, String s) {
        this.time = time;
        image = new GreenfootImage (s);
        image.scale((int)(image.getWidth()*1.0/3.0), (int)(image.getHeight()*1.0/3.0));
    }


    public void act()
    {
        setImage(this.image);
        // Add your action code here.
        if (time == 0) {

            getWorld().removeObject(this);
        }
        else {
            time --; 
        }
        
        return;
    }
}
