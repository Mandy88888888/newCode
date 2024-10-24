import greenfoot.*;
// Import necessary Greenfoot classes (World, Actor, GreenfootImage, etc.)
import java.util.Collections;
import java.util.ArrayList;

/**
 * VehicleWorld is the main game world for the Greenfoot simulation involving
 * vehicles and pedestrians. This world includes lane management, vehicle
 * spawning, and interaction between various game objects such as vehicles,
 * sheep, and wolves.
 * 
 * Key Features: 
 * - Switch lane: with a collasion detector
 * - when sheeps (pedestrian) collade with each other they will move apart
 * - When "click here" is clicked, there will be "red wolves" spawn the hits the Wolf
 * - Night Mode after a certain amount of time
 * - Some sheep have extra blood
 * - After a certain amount of sheep dies, super vehicle will be spawned to revise all the sheep
 * 
 * This class manages the overall game environment and core game loop actions.
 * 
 * 
 * Credits:
 * 
 * LambChop.png - Creative Power Entertaining, Pleasant Goat and Big Big Wolf S1 EP1
 * RoastedLamb.png - Creative Power Entertaining, Pleasant Goat and Big Big Wolf S1 EP1
 * ShabuMutton.png - Creative Power Entertaining, Pleasant Goat and Big Big Wolf S1 EP1
 * SuperVehicle.png - Me
 * download.png - ChunRiChiovo, DuiTang
 * background.png - Jwb123, HuijiWiki
 * DayRoad.png - brgfx, freepix
 * NightRoad.png - upklyak, freepix
 * LanyangYang.png - SweetStatic, DuiTang
 * XiYangYang.png - SweetStatic, DuiTang
 * ManYangYang.png - SweetStatic, DuiTang
 * MeiYangYang.png - SweetStatic, DuiTang
 * HuiTaiLang.png - SweetStatic, DuiTang
 * XiaoHuiHui.png - SweetStatic, DuiTang
 * JiaoTaiLang.png - SweetStatic, DuiTang
 * RedWolf.png - Wangzimu0723, HuijiWiki
 * RedWolf2.png - Creative Power Entertaining, Pleasant Goat and Big Big Wolf: Joys of Seasons| EP 04 3:24
 * Minus.png - Pinterest
 * 
 * DaySound.mp3 - mixkit.co
 * NightSound.mp3 - mixkit.co
 * Slap.mp3 -  Alex_Jauk, pixabay
 * mie.mp3 - zapsplat
 * eat.mp3 - zapsplat
 * vehicle.mp3 - zapsplat
 * revise.mp3 - Pixabay, Pixabay
 * Click.mp3 - zapsplat
 *
 */
public class VehicleWorld extends World {
    private GreenfootImage background; // Image used for the background

    // Color Constants for road drawing
    public static Color GREY_BORDER = new Color(108, 108, 108); // Road border
                                                                // color
    public static Color GREY_STREET = new Color(88, 88, 88); // Street/road
                                                                // color
    public static Color YELLOW_LINE = new Color(255, 216, 0); // Divider line
                                                                // color
                                                                // (yellow)

    // Toggle flags for gameplay mechanics
    public static boolean SHOW_SPAWNERS = false; // Show spawners in debug mode
    public static boolean noEvent = true; // Flag to control event-based
                                            // spawning

    // Constants for Y positions for pedestrians
    public static final int TOP_SPAWN = 325; // Y-coordinate for pedestrians at
                                                // the top
    public static final int BOTTOM_SPAWN = 705; // Y-coordinate for pedestrians
                                                // at the bottom

    // Instance variables for traffic lane management
    private boolean twoWayTraffic, splitAtCenter; // Booleans to control lane
                                                    // flow configuration
    private static final int laneHeight = 48; // The height of each lane
    private static final int laneCount = 6; // The number of lanes
    private static final int spaceBetweenLanes = 8; // Space between each lane
    private int[] lanePositionsY; // Array to hold the Y-positions of lanes
    private VehicleSpawner[] laneSpawners; // Array of VehicleSpawner objects
                                            // for each lane

    // Sound management
    private GreenfootSound sound; // Background ambiamce sound

    // Game mechanics and counters
    public static int sheepDeadCounter = 0; // Counter for dead sheep
    private boolean nightMode = false; // Boolean flag to track day/night mode
    public static int actCounter = 1; // Counter for game ticks (not 0 because 0/3600 R 0)
    private TextButton spawnButton; // Button to spawn vehicles
    private boolean redMode = false; // Boolean flag to toggle "Red Mode"

    /**
     * Constructor for VehicleWorld. Initializes the world settings, sets up
     * lanes, and configures the background. The constructor is executed when
     * the program starts or when the world is reset.
     */
    public VehicleWorld() {
        // Create a new world with dimensions of 1024x800 pixels, with no
        // boundary restrictions (unbounded).
        super(1024, 800, 1, false);

        // Define the paint order for objects in the world. Objects listed first
        // are painted on top.
        // This is commented out to use a z-sorting method for more dynamic
        // layering.
        // setPaintOrder (Pedestrian.class, Vehicle.class);

        // Set up the background image for the world. Make sure the image
        // dimensions match the world.
        background = new GreenfootImage("download.png");
        setBackground(background);

        // Initialize a button that will allow users to spawn vehicles manually.
        spawnButton = new TextButton("Click Here", 24);
        addObject(spawnButton, 60, 24); // Add the button at the top left
                                        // (60,24) of the screen

        // Initialize critical variables related to lane drawing.
        splitAtCenter = false; // No lane splitting at the center (for two-way
                                // traffic)
        twoWayTraffic = false; // Lanes will not be configured for two-way
                                // traffic by default

        // Initialize the spawner objects for each lane. Spawners control entity
        // spawning.
        laneSpawners = new VehicleSpawner[laneCount];

        // Call method to prepare the lanes with the provided parameters.
        // This will create the lanes and configure them based on the set
        // variables.
        lanePositionsY = prepareLanes(this, background, laneSpawners, 400 - 20, laneHeight, laneCount,
                spaceBetweenLanes, twoWayTraffic, splitAtCenter, new GreenfootImage("road.png"));

        // Reapply the background after lane preparation in case it was modified
        // during lane setup.
        setBackground(background);
        
        // Sound management
        sound = new GreenfootSound("DaySound.mp3"); 
    }

    /**
     * Switch between day and night mode. This method will change the background
     * image and redraw the lanes with the appropriate road image (day or night)
     * depending on the current mode.
     */
    private void switchBackground() {
        GreenfootImage image; // Variable to hold the new background image
        sound.stop();
        // If it's currently night mode, switch to day mode, otherwise switch to
        // night mode.
        if (nightMode) {
            image = new GreenfootImage("download.png"); // Day mode background
            sound = new GreenfootSound("DaySound.mp3"); // Day mode sound
        } else {
            image = new GreenfootImage("background.png"); // Night mode
                                                            // background
            sound = new GreenfootSound("NightSound.mp3"); // Night mode sou                                                
        }

        // Prepare the lanes again with the new background and road style.
        prepareLanes(this, image, laneSpawners, 400 - 20, laneHeight, laneCount, spaceBetweenLanes, twoWayTraffic,
                splitAtCenter, new GreenfootImage(nightMode ? "road.png" : "NightRoad.png"));

        // Set the newly selected background.
        setBackground(image);

        // Toggle the night mode flag.
        nightMode = !nightMode;
    }

    /**
     * Gets the height of a single lane.
     * 
     * @return the height of a lane, which is a fixed value of 48.
     */
    public static int getLaneHeight() {
        return laneHeight;
    }

    /**
     * Gets the space between lanes.
     * 
     * @return the space between lanes, which is a fixed value of 8.
     */
    public static int getLaneSpace() {
        return spaceBetweenLanes;
    }

    /**
     * Retrieves the VehicleSpawner object for a specified lane. This method is
     * useful for switching lanes
     * 
     * @param lane
     *            the lane index for which to retrieve the VehicleSpawner.
     * @return the VehicleSpawner object associated with the specified lane.
     */
    public VehicleSpawner getOrigin(int lane) {
        return laneSpawners[lane];
    }

    /**
     * This method is called continuously during each frame (or act cycle) of
     * the game. It handles the main game logic such as sound, spawning
     * vehicles, checking conditions for events like spawning super vehicles,
     * switching background themes, and handling mouse input. The method also
     * ensures that actors are sorted by Z-order for proper visual layering on
     * the screen.
     */
    public void act() {
        // Play the background sound in a continuous loop during the game's act
        // cycle.
        sound.playLoop();

        // Check if no event (like a super vehicle spawn) is active.
        if (noEvent) {
            // If certain number of sheep have been "dead" (hit), spawn a super vehicle.
            if (sheepDeadCounter >= 5) {
                spawnSuperVehicle();
            } else {
                // If redMode is activated, spawn red wolves and wolves only.
                // Otherwise, spawn regular sheeps.
                if (redMode) {
                    redSpawn();
                } else {
                    spawn();
                }
            }
        }

        // Every 3600 acts (frames), switch the background to change themes
        // (e.g., day/night mode).
        if (actCounter % 3600 == 0) {
            switchBackground();
        }

        // If the spawnButton is clicked by the player, toggle red mode.
        if (Greenfoot.mouseClicked(spawnButton)) {
            // play the mouse clicking sound
            Greenfoot.playSound("Click.mp3");
            changeRedMode(); // switch to red mode
        }

        // Increment the act counter by 1 for each frame.
        actCounter++;

        // Sort all Actor objects by their Z-order to ensure correct visual
        // stacking on the screen.
        zSort((ArrayList<Actor>) (getObjects(Actor.class)), this);
    }


    /**
     * Toggles the red mode on or off. Red mode is responsible for spawning
     * RedWolves. When activated, regular vehicle spawning is overridden and
     * only red wolves appear.
     */
    private void changeRedMode() {
        // If red mode is currently active, turn it off; otherwise, activate it.
        if (redMode) {
            redMode = false;
        } else {
            redMode = true;
        }
    }

    /**
     * Spawns red wolves (in red mode) on the road as pedestrians. Two types of
     * wolves are spawned: regular Wolves and RedWolves. Regular Wolves spawn
     * randomly in lanes, while RedWolves spawn randomly at the top or bottom
     * pedestrian zones.
     */
    private void redSpawn() {
        // Random chance to spawn a regular Wolf in one of the lanes.
        // Each lane has a 1 in (laneCount * 10) chance for a Wolf to spawn.
        if (Greenfoot.getRandomNumber(laneCount * 10) == 0) {
            // Select a random lane.
            int lane = Greenfoot.getRandomNumber(laneCount);

            // Check if the selected lane's spawner is not currently touching
            // another vehicle.
            if (!laneSpawners[lane].isTouchingVehicle()) {
                // Add a new Wolf to the world at the location of the selected
                // spawner.
                addObject(new Wolf(laneSpawners[lane], this), 0, 0);
            }
        }

        // Random chance (1 in 60) to spawn a RedWolf in the pedestrian area.
        if (Greenfoot.getRandomNumber(60) == 0) {
            // Determine a random horizontal spawn location between 100 and 699,
            // away from edges.
            int xSpawnLocation = Greenfoot.getRandomNumber(600) + 100;

            // Randomly decide whether to spawn the RedWolf at the top or bottom
            // of the screen.
            boolean spawnAtTop = Greenfoot.getRandomNumber(2) == 0 ? true : false;

            // Spawn a RedWolf either at the top pedestrian area or the bottom,
            // based on the random boolean.
            if (spawnAtTop) {
                // Spawn at top
                addObject(new RedWolf(1), xSpawnLocation, TOP_SPAWN); 
            } else {
                // Spawn at bottom
                addObject(new RedWolf(-1), xSpawnLocation, BOTTOM_SPAWN); 
            }
        }
    }

    /**
     * Spawns a "SuperVehicle" in all lanes that are clear. This method
     * overrides the default vehicle spawn and only happens when a specific
     * event condition is met (when `sheepDeadCounter >= 3`). Once this happens,
     * no regular vehicles will spawn until this event is over.
     * 
     * Each lane's spawner is checked for the presence of another vehicle, and
     * if clear, a SuperVehicle is added to that lane.
     */
    private void spawnSuperVehicle() {
        // Play sound FX for super vehicle
        Greenfoot.playSound("vehicle.mp3"); 
        // Disable regular spawning events when a SuperVehicle is being spawned.
        noEvent = false;

        // Loop through all lanes and spawn a SuperVehicle in lanes that are
        // clear.
        for (int lane = 0; lane < laneCount; lane++) {
            // Check if the lane's spawner is not currently touching another
            // vehicle.
            if (!laneSpawners[lane].isTouchingVehicle()) {
                // Add a new SuperVehicle object to the world at the location of
                // the lane's spawner.
                addObject(new SuperVehicle(laneSpawners[lane], this), 0, 0);
            }
        }
    }

    /**
     * Spawns regular vehicles and sheep in the world. This is the default
     * spawning mechanism. Vehicles are spawned in lanes, while sheep are
     * spawned either at the top or bottom pedestrian zones.
     * 
     * There are different types of vehicles (Wolf, LittleWolf, BananaWolf), and
     * similarly, sheep can be of different types (SmartSheep, CuteSheep,
     * SlowSheep, PrettySheep), which spawn randomly in their respective zones.
     */
    private void spawn() {
        // Random chance (1 in laneCount * 10) to spawn a vehicle in one of the
        // lanes.
        if (Greenfoot.getRandomNumber(laneCount * 10) == 0) {
            // Select a random lane.
            int lane = Greenfoot.getRandomNumber(laneCount);

            // Check if the lane's spawner is not currently touching another
            // vehicle.
            if (!laneSpawners[lane].isTouchingVehicle()) {
                // Randomly select the type of vehicle to spawn (Wolf,
                // LittleWolf, or BananaWolf).
                int vehicleType = Greenfoot.getRandomNumber(3);
                if (vehicleType == 0) {
                    addObject(new Wolf(laneSpawners[lane], this), 0, 0); // Spawn
                                                                            // a
                                                                            // Wolf
                } else if (vehicleType == 1) {
                    addObject(new LittleWolf(laneSpawners[lane], this), 0, 0); // Spawn
                                                                                // a
                                                                                // LittleWolf
                } else if (vehicleType == 2) {
                    addObject(new BananaWolf(laneSpawners[lane], this), 0, 0); // Spawn
                                                                                // a
                                                                                // BananaWolf
                }
            }
        }

        // Random chance (1 in 60) to spawn a Sheep in the pedestrian area.
        if (Greenfoot.getRandomNumber(60) == 0) {
            // Determine a random horizontal spawn location between 100 and 699,
            // away from edges.
            int xSpawnLocation = Greenfoot.getRandomNumber(600) + 100;

            // Randomly decide whether to spawn the sheep at the top or bottom
            // of the screen.
            boolean spawnAtTop = Greenfoot.getRandomNumber(2) == 0;

            // Randomly select the type of sheep to spawn (SmartSheep,
            // CuteSheep, SlowSheep, or PrettySheep).
            
            // Random number between 1 and 4
            int choice = (int) Math.round(Math.random() * 3) + 1; 

            // Spawn the sheep either at the top or bottom pedestrian zone.
            if (spawnAtTop) {
                // Spawn the chosen type of sheep at the top pedestrian area.
                switch (choice) {
                case 1:
                    addObject(new SmartSheep(1), xSpawnLocation, TOP_SPAWN);
                    break;
                case 2:
                    // cute sheep (a.k.a lazy sheep)doesn't come out at night
                    if (!nightMode) {
                        addObject(new CuteSheep(1), xSpawnLocation, TOP_SPAWN);
                    }
                    break;

                case 3:
                    // slow sheep doesn't come out at night
                    if (!nightMode) {
                        addObject(new SlowSheep(1), xSpawnLocation, TOP_SPAWN);
                    }

                case 4:
                    addObject(new PrettySheep(1), xSpawnLocation, TOP_SPAWN);
                    break;
                }
            } else {
                // Spawn the chosen type of sheep at the bottom pedestrian area.
                switch (choice) {
                case 1:
                    addObject(new SmartSheep(-1), xSpawnLocation, BOTTOM_SPAWN);
                    break;
                case 2:
                    // cute sheep (a.k.a lazy sheep)doesn't come out at night
                    if (!nightMode) {
                        addObject(new CuteSheep(-1), xSpawnLocation, BOTTOM_SPAWN);
                    }
                    break;

                case 3:
                    // slow sheep doesn't come out at night
                    if (!nightMode) {
                        addObject(new SlowSheep(-1), xSpawnLocation, BOTTOM_SPAWN);
                    }
                    break;
                case 4:
                    addObject(new PrettySheep(-1), xSpawnLocation, BOTTOM_SPAWN);
                    break;
                }
            }
        }
    }

    /**
     * Given a lane number (zero-indexed), return the y position in the centre
     * of the lane. (doesn't factor offset, so watch your offset, i.e. with
     * Bus).
     * 
     * @param lane
     *            the lane number (zero-indexed)
     * @return int the y position of the lane's center, or -1 if invalid
     */
    public int getLaneY(int lane) {
        if (lane < lanePositionsY.length) {
            return lanePositionsY[lane];
        }
        return -1;
    }

    /**
     * Given a y-position, return the lane number (zero-indexed). Note that the
     * y-position must be valid, and you should include the offset in your
     * calculations before calling this method. For example, if a Bus is in a
     * lane at y=100, but is offset by -20, it is actually in the lane located
     * at y=80, so you should send 80 to this method, not 100.
     * 
     * @param y
     *            - the y position of the lane the Vehicle is in
     * @return int the lane number, zero-indexed
     * 
     */
    public int getLane(int y) {
        for (int i = 0; i < lanePositionsY.length; i++) {
            if (y == lanePositionsY[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Prepares the lanes for the world, calculates lane positions, places
     * vehicle spawners, and draws lane lines and backgrounds based on traffic
     * flow type (one-way, two-way, etc.).
     * 
     * This method is highly flexible, supporting different configurations for
     * traffic lanes: - Two-way traffic, with or without a central split. -
     * One-way traffic. - Configurable lane heights, spacings, and center
     * spacing.
     * 
     * The spawners are vehicle generators placed on each lane, and the method
     * also handles the graphical drawing of lane lines (yellow for center
     * dividers and white for lane dividers).
     * 
     * @param world
     *            The Greenfoot world in which the lanes and spawners will be
     *            placed.
     * @param target
     *            The GreenfootImage used for drawing the lane backgrounds and
     *            dividers.
     * @param spawners
     *            An array of VehicleSpawner objects, one for each lane.
     * @param startY
     *            The starting Y-coordinate for the first lane.
     * @param heightPerLane
     *            The height of each lane.
     * @param lanes
     *            The total number of lanes to create.
     * @param spacing
     *            The space between lanes.
     * @param twoWay
     *            Whether the lanes should be for two-way traffic.
     * @param centreSplit
     *            Whether the lanes should have a central split (used for
     *            two-way traffic).
     * @param centreSpacing
     *            The spacing in the center (for two-way, center-split roads).
     * @param background
     *            The background image for the road.
     * 
     * @return int[] Returns an array of Y positions for each lane.
     */

    public static int[] prepareLanes(World world, GreenfootImage target, VehicleSpawner[] spawners, int startY,
            int heightPerLane, int lanes, int spacing, boolean twoWay, boolean centreSplit, int centreSpacing,
            GreenfootImage background) {
        // Declare an array to store the y values as I calculate them
        int[] lanePositions = new int[lanes];
        // Pre-calculate half of the lane height, as this will frequently be
        // used for drawing.
        // To help make it clear, the heightOffset is the distance from the
        // centre of the lane (it's y position)
        // to the outer edge of the lane.
        int heightOffset = heightPerLane / 2;
        // add the picture of the background
        target.drawImage(background, 0, startY);
        // Main Loop to Calculate Positions and draw lanes
        for (int i = 0; i < lanes; i++) {
            // calculate the position for the lane
            lanePositions[i] = startY + spacing + (i * (heightPerLane + spacing)) + heightOffset;

            // Place spawners and draw lines depending on whether its 2 way and
            // centre split
            if (twoWay && centreSplit) {
                // first half of the lanes go rightward (no option for left-hand
                // drive, sorry UK students .. ?)
                if (i < lanes / 2) {
                    spawners[i] = new VehicleSpawner(false, heightPerLane, i);
                    world.addObject(spawners[i], target.getWidth(), lanePositions[i]);
                } else { // second half of the lanes go leftward
                    spawners[i] = new VehicleSpawner(true, heightPerLane, i);
                    world.addObject(spawners[i], 0, lanePositions[i]);
                }

                // draw yellow lines if middle
                if (i == lanes / 2) {
                    target.setColor(YELLOW_LINE);
                    target.fillRect(0, lanePositions[i] - heightOffset - spacing, target.getWidth(), spacing);

                } else if (i > 0) { // draw white lines if not first lane
                    for (int j = 0; j < target.getWidth(); j += 120) {
                        target.setColor(Color.WHITE);
                        target.fillRect(j, lanePositions[i] - heightOffset - spacing, 60, spacing);
                    }
                }

            } else if (twoWay) { // not center split
                if (i % 2 == 0) {
                    spawners[i] = new VehicleSpawner(false, heightPerLane, i);
                    world.addObject(spawners[i], target.getWidth(), lanePositions[i]);
                } else {
                    spawners[i] = new VehicleSpawner(true, heightPerLane, i);
                    world.addObject(spawners[i], 0, lanePositions[i]);
                }

                // draw Grey Border if between two "Streets"
                if (i > 0) { // but not in first position
                    if (i % 2 == 0) {
                        target.setColor(GREY_BORDER);
                        target.fillRect(0, lanePositions[i] - heightOffset - spacing, target.getWidth(), spacing);

                    } else { // draw dotted lines
                        for (int j = 0; j < target.getWidth(); j += 120) {
                            target.setColor(YELLOW_LINE);
                            target.fillRect(j, lanePositions[i] - heightOffset - spacing, 60, spacing);
                        }
                    }
                }
            } else { // One way traffic
                spawners[i] = new VehicleSpawner(true, heightPerLane, i);
                world.addObject(spawners[i], 0, lanePositions[i]);
                if (i > 0) {
                    for (int j = 0; j < target.getWidth(); j += 120) {
                        target.setColor(Color.WHITE);
                        target.fillRect(j, lanePositions[i] - heightOffset - spacing, 60, spacing);
                    }
                }
            }
        }

        return lanePositions;
    }

    /**
     * Method when world is paused that will stop the background music
     */

    public void stopped() {
        sound.pause();
    }

    /**
     * A z-sort method which will sort Actors so that Actors that are displayed
     * "higher" on the screen (lower y values) will show up underneath Actors
     * that are drawn "lower" on the screen (higher y values), creating a better
     * perspective.
     */
    public static void zSort(ArrayList<Actor> actorsToSort, World world) {
        ArrayList<ActorContent> acList = new ArrayList<ActorContent>();
        // Create a list of ActorContent objects and populate it with all Actors
        // sent to be sorted
        for (Actor a : actorsToSort) {
            acList.add(new ActorContent(a, a.getX(), a.getY()));
        }
        // Sort the Actor, using the ActorContent comparitor (compares by y
        // coordinate)
        Collections.sort(acList);
        // Replace the Actors from the ActorContent list into the World,
        // inserting them one at a time
        // in the desired paint order (in this case lowest y value first, so
        // objects further down the
        // screen will appear in "front" of the ones above them).
        for (ActorContent a : acList) {
            Actor actor = a.getActor();
            world.removeObject(actor);
            world.addObject(actor, a.getX(), a.getY());
        }
    }

    /**
     * <p>
     * The prepareLanes method is a static (standalone) method that takes a list
     * of parameters about the desired roadway and then builds it.
     * </p>
     * 
     * <p>
     * <b>Note:</b> So far, Centre-split is the only option, regardless of what
     * values you send for that parameters.
     * </p>
     *
     * <p>
     * This method does three things:
     * </p>
     * <ul>
     * <li>Determines the Y coordinate for each lane (each lane is centered
     * vertically around the position)</li>
     * <li>Draws lanes onto the GreenfootImage target that is passed in at the
     * specified / calculated positions. (Nothing is returned, it just
     * manipulates the object which affects the original).</li>
     * <li>Places the VehicleSpawners (passed in via the array parameter
     * spawners) into the World (also passed in via parameters).</li>
     * </ul>
     * 
     * <p>
     * After this method is run, there is a visual road as well as the objects
     * needed to spawn Vehicles. Examine the table below for an in-depth
     * description of what the roadway will look like and what each
     * parameter/component represents.
     * </p>
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
     * @param world
     *            The World that the VehicleSpawners will be added to
     * @param target
     *            The GreenfootImage that the lanes will be drawn on, usually
     *            but not necessarily the background of the World.
     * @param spawners
     *            An array of VehicleSpawner to be added to the World
     * @param startY
     *            The top Y position where lanes (drawing) should start
     * @param heightPerLane
     *            The height of the desired lanes
     * @param lanes
     *            The total number of lanes desired
     * @param spacing
     *            The distance, in pixels, between each lane
     * @param twoWay
     *            Should traffic flow both ways? Leave false for a one-way
     *            street (Not Yet Implemented)
     * @param centreSplit
     *            Should the whole road be split in the middle? Or lots of
     *            parallel two-way streets? Must also be two-way street (twoWay
     *            == true) or else NO EFFECT
     * 
     */
    public static int[] prepareLanes(World world, GreenfootImage target, VehicleSpawner[] spawners, int startY,
            int heightPerLane, int lanes, int spacing, boolean twoWay, boolean centreSplit, GreenfootImage image) {
        return prepareLanes(world, target, spawners, startY, heightPerLane, lanes, spacing, twoWay, centreSplit,
                spacing, image);
    }

}

/**
 * A container class that holds an Actor and its local position (X and Y
 * coordinates).
 * 
 * This class is used to store the position of an Actor, which can be useful
 * when the Actor needs to be temporarily removed from the World, and you want
 * to preserve its position for later.
 * 
 * Additionally, the class implements the Comparable interface, allowing
 * ActorContent objects to be sorted by their Y-coordinate. This is useful for
 * Z-ordering, where actors need to be rendered in the correct order based on
 * their vertical position.
 */
class ActorContent implements Comparable<ActorContent> {
    private Actor actor; // The Actor instance that this class is holding.
    private int xx, yy; // The X and Y coordinates of the Actor.

    /**
     * Constructor for ActorContent.
     * 
     * @param actor
     *            The Actor instance to be stored.
     * @param xx
     *            The X-coordinate of the Actor.
     * @param yy
     *            The Y-coordinate of the Actor.
     */
    public ActorContent(Actor actor, int xx, int yy) {
        this.actor = actor;
        this.xx = xx;
        this.yy = yy;
    }

    /**
     * Updates the location of the ActorContent to new coordinates.
     * 
     * @param x
     *            The new X-coordinate.
     * @param y
     *            The new Y-coordinate.
     */
    public void setLocation(int x, int y) {
        xx = x;
        yy = y;
    }

    /**
     * Returns the X-coordinate of the stored Actor.
     * 
     * @return The X-coordinate as an integer.
     */
    public int getX() {
        return xx;
    }

    /**
     * Returns the Y-coordinate of the stored Actor.
     * 
     * @return The Y-coordinate as an integer.
     */
    public int getY() {
        return yy;
    }

    /**
     * Returns the Actor instance held by this container.
     * 
     * @return The Actor object.
     */
    public Actor getActor() {
        return actor;
    }

    /**
     * Provides a string representation of the ActorContent, showing the Actor
     * and its position (X and Y coordinates).
     * 
     * @return A string describing the Actor and its location.
     */
    public String toString() {
        return "Actor: " + actor + " at " + xx + ", " + yy;
    }

    /**
     * Compares this ActorContent object with another ActorContent object based
     * on the Y-coordinate. This allows ActorContent instances to be sorted by
     * their vertical position.
     * 
     * @param a
     *            The other ActorContent object to compare to.
     * @return An integer: negative if this ActorContent has a smaller
     *         Y-coordinate, zero if equal, or positive if greater.
     */
    public int compareTo(ActorContent a) {
        return this.getY() - a.getY();
    }
}
