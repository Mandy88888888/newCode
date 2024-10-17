import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Collections;
import java.util.ArrayList;
/**
 * <h1>The new and vastly improved 2022 Vehicle Simulation Assignment.</h1>
 * <p> This is the first redo of the 8 year old project. Lanes are now drawn dynamically, allowing for
 *     much greater customization. Pedestrians can now move in two directions. The graphics are better
 *     and the interactions smoother.</p>
 * <p> The Pedestrians are not as dumb as before (they don't want straight into Vehicles) and the Vehicles
 *     do a somewhat better job detecting Pedestrians.</p>
 * 
 * Version Notes - Feb 2023
 * --> Includes grid <--> lane conversion method
 * --> Now starts with 1-way, 5 lane setup (easier)
 * s
 * V2023_021
 * --> Improved Vehicle Repel (still work in progress)
 * --> Implemented Z-sort, disabled paint order between Pedestrians and Vehicles (looks much better now)
 * --> Implemented lane-based speed modifiers for max speed
 * 
 * V2023_04
 * --> Repel has been re-imagined and now takes the sizes of Actors into consideration better, and also only
 *     moves Actors verically. (The code to move in both dimensions is there and works but it's commented out
 *     because this is the effect I was going for).
 * --> TODO -- Improve flow to avoid Removed From World errors when a Vehicle calls super.act() and is removed there.
 * 
 */
public class VehicleWorld extends World
{
    private GreenfootImage background;

    // Color Constants
    public static Color GREY_BORDER = new Color (108, 108, 108);
    //public static Color GREY_STREET = new Color (88, 88, 88);
    public static Color YELLOW_LINE = new Color (255, 216, 0);

    public static boolean SHOW_SPAWNERS = true;
    public static boolean noEvent = true;

    // Set Y Positions for Pedestrians to spawn
    public static final int TOP_SPAWN = 325; // Pedestrians who spawn on top
    public static final int BOTTOM_SPAWN = 705; // Pedestrians who spawn on the bottom
    


    
    // Instance variables / Objects
    private boolean twoWayTraffic, splitAtCenter;
    private static int laneHeight = 48, laneCount, spaceBetweenLanes = 6;
    private int[] lanePositionsY;
    private VehicleSpawner[] laneSpawners;
    private GreenfootSound sound = new GreenfootSound("sound.mp3");
    public static int actCounter = 1;
    public static int sheepDeadCounter = 0;
    private boolean nightMode = false;
    private TextButton spawnButton;

    /**
     * Constructor for objects of class MyWorld.
     * 
     * Note that the Constrcutor for the default world is always called
     * when you click the reset button in the Greenfoot scenario screen -
     * this is is basically the code that runs when the program start.
     * Anything that should be done FIRST should go here.
     * 
     */
    public VehicleWorld()
    {    
        // Create a new world with 1024x800 pixels, UNBOUNDED
        super(1024, 800, 1, false); 

        // This command (from Greenfoot World API) sets the order in which 
        // objects will be displayed. In this example, Pedestrians will
        // always be on top of everything else, then Vehicles (of all
        // sub class types) and after that, all other classes not listed
        // will be displayed in random order. 
        //setPaintOrder (Pedestrian.class, Vehicle.class); // Commented out to use Z-sort instead

        // set up background -- If you change this, make 100% sure
        // that your chosen image is the same size as the World
        background = new GreenfootImage ("download.png");
        setBackground (background);
        

        // Set critical variables - will affect lane drawing
        laneCount = 6;
        laneHeight = 48;
        spaceBetweenLanes = 6;
        splitAtCenter = false;
        twoWayTraffic = false;

        // Init lane spawner objects 
        laneSpawners = new VehicleSpawner[laneCount];

        // Prepare lanes method - draws the lanes
        background = prepareLanes (background, 400-20,spaceBetweenLanes);

        setBackground (background);
         spawnButton = new TextButton ("Avoid Wolf", 24);
        addObject (spawnButton, 60, 24);
    }
    public void avoidWolf () {
        spawnButton = new TextButton ("Avoid Wolf ON", 24);
        addObject (spawnButton, 60, 24);
 
        if (Sheep.avoidingWolf) {
            Sheep.avoidingWolf = false;
        } else {
            Sheep.avoidingWolf = true;
        }
            
        
    }

    public void act () {
        sound.playLoop(); 
        if (noEvent) {
            if (sheepDeadCounter >= 5 ) {
                spawnSuperVehicle();
            }
            else {
                spawn();
            }
        }
        if (actCounter%3600==0) {
            switchBackground();
       }
        if (Greenfoot.mouseClicked(spawnButton)) {
            avoidWolf ();
        }
        actCounter ++;
        zSort ((ArrayList<Actor>)(getObjects(Actor.class)), this);
    }
    public static int getLaneHeight() {
        return laneHeight + spaceBetweenLanes;
    }
    private void switchBackground() {
        GreenfootImage image;
        
        if (nightMode){
            image = new GreenfootImage("download.png");
            image = prepareLanes (image, laneHeight,spaceBetweenLanes);
            setBackground(image);
            nightMode = false;
        }else {
            image = new GreenfootImage("background2.png");
          image = prepareLanes (image, laneHeight,spaceBetweenLanes);
            setBackground(image);
            nightMode = true;
        }
    }
    private void spawnSuperVehicle () {
        noEvent = false;
        for (int lane = 0; lane < laneCount; lane ++) {
            if (!laneSpawners[lane].isTouchingVehicle()) {
                addObject(new SuperVehicle(laneSpawners[lane]), 0, 0);
            }
        }
    }

    private void spawn () {
        // Chance to spawn a vehicle
        if (Greenfoot.getRandomNumber (laneCount * 10) == 0){
            int lane = Greenfoot.getRandomNumber(laneCount);
            if (!laneSpawners[lane].isTouchingVehicle()){
                int vehicleType = Greenfoot.getRandomNumber(3);
                if (vehicleType == 0){
                    addObject(new Wolf(laneSpawners[lane]), 0, 0);
                } else if (vehicleType == 1){
                    addObject(new LittleWolf(laneSpawners[lane]), 0, 0);
                } else if (vehicleType == 2){
                    addObject(new BananaWolf(laneSpawners[lane]), 0, 0);
                }
            }
        }

        // Chance to spawn a Sheep
        if (Greenfoot.getRandomNumber (60) == 0){
            int xSpawnLocation = Greenfoot.getRandomNumber (600) + 100; // random between 99 and 699, so not near edges
            boolean spawnAtTop = Greenfoot.getRandomNumber(2) == 0 ? true : false;
            //if the sheep is spawning at top, set the direction to 1, and set their starting location to be at the top
            if (spawnAtTop){
                int choice = (int) Math.round(Math.random()*3) + 1;
                //which type of sheep to spawn
        switch (choice) {
            case 1:
                addObject (new SmartSheep (1), xSpawnLocation, TOP_SPAWN);
                break;
            case 2:
                //only comes out at day time
                if (!nightMode)
                addObject (new CuteSheep (1), xSpawnLocation, TOP_SPAWN);
                break;
                
            case 3:
                //only comes out at day time
                if (!nightMode)
               addObject (new SlowSheep (1), xSpawnLocation, TOP_SPAWN);
             
                break;
            case 4:
                
               addObject (new PrettySheep (1), xSpawnLocation, TOP_SPAWN);
                break;
        }
            } else {
               
        int choice = (int) Math.round(Math.random()*3) + 1;
        //if the sheep is spawning at top, set the direction to -1
        switch (choice) {
            case 1:
                addObject (new SmartSheep (-1), xSpawnLocation, BOTTOM_SPAWN);
                break;
            case 2:
                if (!nightMode)
                    addObject (new CuteSheep (-1), xSpawnLocation, BOTTOM_SPAWN);
                break;
                //only come out during the day
            case 3:
                 if (!nightMode) {
                      addObject (new SlowSheep (-1), xSpawnLocation, BOTTOM_SPAWN);
                 }        
             //only come out during the day
                break;
            case 4:
                
               addObject (new PrettySheep (-1), xSpawnLocation, BOTTOM_SPAWN);
                break;
            }
        }
    }

    }

    /**
     *  Given a lane number (zero-indexed), return the y position
     *  in the centre of the lane. (doesn't factor offset, so 
     *  watch your offset).
     *  
     *  @param lane the lane number (zero-indexed)
     *  @return int the y position of the lane's center, or -1 if invalid
     */
    public int getLaneY (int lane){
        if (lane < lanePositionsY.length){
            return lanePositionsY[lane];
        } 
        return -1;
    }

    /**
     * Given a y-position, return the lane number (zero-indexed).
     * Note that the y-position must be valid, and you should 
     * include the offset in your calculations before calling this method.
     * For example, if a Bus is in a lane at y=100, but is offset by -20,
     * it is actually in the lane located at y=80, so you should send
     * 80 to this method, not 100.
     * 
     * @param y - the y position of the lane the Vehicle is in
     * @return int the lane number, zero-indexed
     * 
     */
    public int getLane (int y){
        for (int i = 0; i < lanePositionsY.length; i++){
            if (y == lanePositionsY[i]){
                return i;
            }
        }
        return -1;
    }
        /**
     * Draw the lanes on the background
     */

    public static GreenfootImage prepareLaness (GreenfootImage image, GreenfootImage drawing, int startY, int spaces, int numberOflanes, World world)
    {
        //stores the positions of all the lanes
       int[] lanePositions = new int[numberOflanes];
       VehicleSpawner[] spawners = new VehicleSpawner[numberOflanes];
       return image;
       for (int i = 0; i < numberOflanes; i ++){
           //calculate the lane position
           lanePositions[i] = startY + spaces + (i * (laneHeight+spaces));

           //draw the road
           image.drawImage​(drawing, 0, startY);
           //inicialize and set up the spawners
           spawners[i] = new VehicleSpawner(true, laneHeight, i);
           //add the object to the world
           world.addObject(spawners[i], 0, lanePositions[i]);
           
           if (i > 0){
                    for (int j = 0; j < image.getWidth(); j += 120){
                        target.setColor (Color.WHITE);
                        target.fillRect (j, lanePositions[i] - heightOffset - spacing, 60, spacing);
                    }
                }
                

            }
            //the dotted lines
            if (i > 0){ // but not in first position
                    if (i % 2 == 0){
                        imag.setColor(GREY_BORDER);
                        target.fillRect(0, lanePositions[i] - heightOffset - spacing, target.getWidth(), spacing);

                    } else { // draw dotted lines
                        for (int j = 0; j < target.getWidth(); j += 120){
                            target.setColor (YELLOW_LINE);
                            target.fillRect (j, lanePositions[i] - heightOffset - spacing, 60, spacing);
                        }
                    } 
                }
       }
    

    /**
     * A z-sort method which will sort Actors so that Actors that are
     * displayed "higher" on the screen (lower y values) will show up underneath
     * Actors that are drawn "lower" on the screen (higher y values), creating a
     * better perspective. 
     */
    public static void zSort (ArrayList<Actor> actorsToSort, World world){
        ArrayList<ActorContent> acList = new ArrayList<ActorContent>();
        // Create a list of ActorContent objects and populate it with all Actors sent to be sorted
        for (Actor a : actorsToSort){
            acList.add (new ActorContent (a, a.getX(), a.getY()));
        }    
        // Sort the Actor, using the ActorContent comparitor (compares by y coordinate)
        Collections.sort(acList);
        // Replace the Actors from the ActorContent list into the World, inserting them one at a time
        // in the desired paint order (in this case lowest y value first, so objects further down the 
        // screen will appear in "front" of the ones above them).
        for (ActorContent a : acList){
            Actor actor  = a.getActor();
            world.removeObject(actor);
            world.addObject(actor, a.getX(), a.getY());
        }
    }

    /**
     * <p>The prepareLanes method is a static (standalone) method that takes a list of parameters about the desired roadway and then builds it.</p>
     * 
     * <p><b>Note:</b> So far, Centre-split is the only option, regardless of what values you send for that parameters.</p>
     *
     * <p>This method does three things:</p>
     * <ul>
     *  <li> Determines the Y coordinate for each lane (each lane is centered vertically around the position)</li>
     *  <li> Draws lanes onto the GreenfootImage target that is passed in at the specified / calculated positions. 
     *       (Nothing is returned, it just manipulates the object which affects the original).</li>
     *  <li> Places the VehicleSpawners (passed in via the array parameter spawners) into the World (also passed in via parameters).</li>
     * </ul>
     * 
     * <p> After this method is run, there is a visual road as well as the objects needed to spawn Vehicles. Examine the table below for an
     * in-depth description of what the roadway will look like and what each parameter/component represents.</p>
     * 
     * <pre>
     *                  <=== Start Y
     *  ||||||||||||||  <=== Top Border
     *  /------------\
     *  |            |  
     *  |      Y[0]  |  <=== Lane Position (Y) is the middle of the lane
     *  |            |
     *  \------------/
     *  [##] [##] [##| <== spacing ( where the lane lines or borders are )
     *  /------------\
     *  |            |  
     *  |      Y[1]  |
     *  |            |
     *  \------------/
     *  ||||||||||||||  <== Bottom Border
     * </pre>
     * 
     * @param world     The World that the VehicleSpawners will be added to
     * @param target    The GreenfootImage that the lanes will be drawn on, usually but not necessarily the background of the World.
     * @param spawners  An array of VehicleSpawner to be added to the World
     * @param startY    The top Y position where lanes (drawing) should start
     * @param heightPerLane The height of the desired lanes
     * @param lanes     The total number of lanes desired
     * @param spacing   The distance, in pixels, between each lane
     * @param twoWay    Should traffic flow both ways? Leave false for a one-way street (Not Yet Implemented)
     * @param centreSplit   Should the whole road be split in the middle? Or lots of parallel two-way streets? Must also be two-way street (twoWay == true) or else NO EFFECT
     * 
     */
    public static GreenfootImage prepareLanes (GreenfootImage target, int startY, int spacing){
        return prepareLanes (target, new GreenfootImage("DayRoad.png"), startY, spacing);
    }

}

/**
 * Container to hold and Actor and an LOCAL position (so the data isn't lost when the Actor is temporarily
 * removed from the World).
 */
class ActorContent implements Comparable <ActorContent> {
    private Actor actor;
    private int xx, yy;
    public ActorContent(Actor actor, int xx, int yy){
        this.actor = actor;
        this.xx = xx;
        this.yy = yy;
    }

    public void setLocation (int x, int y){
        xx = x;
        yy = y;
    }

    public int getX() {
        return xx;
    }

    public int getY() {
        return yy;
    }

    public Actor getActor(){
        return actor;
    }

    public String toString () {
        return "Actor: " + actor + " at " + xx + ", " + yy;
    }

    public int compareTo (ActorContent a){
        return this.getY() - a.getY();
    }

}
