import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;


/**
 * This is the superclass for Vehicles.
 * It defines common attributes and behaviors for all vehicle types.
 */
public abstract class Vehicle extends SuperSmoothMover {

    // Attributes
    protected boolean waiting = false; // Indicates if the vehicle is waiting
    protected int timer = 0; // Timer for the waiting period
    protected double maxSpeed; // Maximum speed of the vehicle
    protected double speed; // Current speed of the vehicle
    protected int direction; // Direction of movement (1 = right, -1 = left)
    protected boolean moving; // Indicates if the vehicle is currently moving
    protected boolean isNew; // Flag to check if the vehicle is new
    protected int yOffset; // Y offset for positioning
    protected VehicleSpawner origin; // Reference to the spawner that created this vehicle
    protected int followingDistance; // Distance to maintain from the vehicle ahead
    protected int myLaneNumber; // Lane number where the vehicle is located
    protected long hitPedestrian = 0; // Counter for pedestrian collision
    protected int counter = 100; // Default count for pedestrian-related behavior
    protected int bombCounter = 300; // Counter related to bomb interactions
    private boolean switchingLane = false; // Indicates if the vehicle is currently switching lanes
    private int switchLaneCounter = 0; // Counter for lane-switching duration
    private VehicleWorld world; // Reference to the vehicle world
    private int switchLaneFactor; // Factor affecting the speed of lane switching
    protected VehicleDetector u = new VehicleDetector(this, true); // Upper detector for pedestrians
    protected VehicleDetector d = new VehicleDetector(this, false); // Lower detector for pedestrians
    private int switchlanePossibility = 1000;

    // Abstract method to check if the vehicle has hit a pedestrian
    protected abstract boolean checkHitPedestrian();



    public Vehicle(VehicleSpawner origin, VehicleWorld world) {
        // remember the VehicleSpawner I came from. This includes information
        // about which lane I'm in and which direction I should face
        this.origin = origin;
        moving = true;
        this.world = world;
        // inicialize the vehicle detectors

        // ask the Spawner that spawned me what my lane number is
        myLaneNumber = origin.getLaneNumber();
        // Determine if this lane is facing towards the right and
        // set the direction accordingly
        if (origin.facesRightward()) { // Right facing vehicles
            direction = 1;
        } else { // left facing Vehicles
            direction = -1;
            // Reverse the image so it appears correct when moving the opposite
            // direction
            getImage().mirrorHorizontally();
        }
        // If speed modifiers were set for lanes, this will change the max speed
        // accordingly. If speed modifiers are not set, this multiplies by 1.0
        // (as in,
        // does nothing).
        maxSpeed *= (double) Math.round(Math.random() * 100) / 100;
        speed = maxSpeed;

        isNew = true; // this boolean serves to make sure this Vehicle is only
                        // placed in
                        // it's starting position once. Vehicles are removed and
                        // re-added
                        // to the world (instantly, not visibly) by the z-sort,
                        // and without this,
                        // they would continue to return to their start points.
    }

    /**
     * This method is called automatically when the Vehicle is added to the
     * World, and places the Vehicle just off screen (centered 100 pixels beyond
     * the center of the lane spawner) so it will appear to roll onto the screen
     * smoothly.
     *
     * This method also adds detectors for the vehicle.
     * Detectors are used to check for pedestrians in the vehicle's vicinity.
     */
    public void addedToWorld(World w) {
        if (isNew) {
            // Set the initial location of the vehicle based on the lane number
            // and direction. This ensures the vehicle starts just off-screen
            setLocation(origin.getX() - (direction * 100), origin.getY() - yOffset);
            isNew = false; // Mark the vehicle as no longer new
            addDetectors(); // Initialize the vehicle detectors
        }
    }

    /**
     * This method checks for the possibility of switching lanes based on the 
     * detection of other vehicles. It changes the lane number if the adjacent 
     * lane is free, and sets the necessary counters for the lane-switching animation.
     *
     * This method also adds detectors for the vehicle.
     * Detectors are used to check for pedestrians in the vehicle's vicinity.
     */
    public void switchLane() {
        if (!u.isDetected() && myLaneNumber != 0) {
            switchlanePossibility *= 10; // Everytime a lane is switched, the possibility 
                                        // of switching lane increase by 10 times
            // If there's no vehicle above and not in the top lane
            switchingLane = true; // Set lane switching status
            switchLaneFactor = 3; // Set the switch lane factor
            // Calculate the lane switching counter based on lane height and space
            switchLaneCounter = (VehicleWorld.getLaneHeight() + VehicleWorld.getLaneSpace()) / switchLaneFactor;
            myLaneNumber -= 1; // Move up to the lane above
            switchLaneFactor = -3; // Reset the factor for next switch
            origin = world.getOrigin(myLaneNumber);  // Reset the origin vehicle spawner 
        } else if (!d.isDetected() && myLaneNumber != 5) {
            // If there's no vehicle below and not in the bottom lane
            switchingLane = true; // Set lane switching status
            switchLaneFactor = 3; // Set the switch lane factor
            // Calculate the lane switching counter based on lane height
            switchLaneCounter = VehicleWorld.getLaneHeight() / switchLaneFactor;
            myLaneNumber += 1; // Move down to the lane below
            origin = world.getOrigin(myLaneNumber);// Reset the origin vehicle spawner 
        }
    }
    public int getLaneNumber() {
        return myLaneNumber;
    }

    /**
     * This method adds detectors for the vehicle.
     * Detectors are used to check for pedestrians in the vehicle's vicinity.
     */
    private void addDetectors() {
        // if the vehicle is spawn in the first lane only spawn a lower vehicle
        if (myLaneNumber == 0) {
            getWorld().addObject(d, getX(), getY());
        }
        // if the vehicle is spawn in the 5th lane only spawn an upper vehicle
        else if (myLaneNumber == 5) {
            getWorld().addObject(u, getX(), getY());
        }
        // if the vechile is spawn in one of the middle lanes, spawn both
        // detectors
        else {
            getWorld().addObject(u, getX(), getY());
            getWorld().addObject(d, getX(), getY());
        }

    }

    /**
     * This method sets the vehicle to wait for a specified number of seconds 
     * before it can move again. It updates the timer and sets the waiting status.
     *
     * This method also adds detectors for the vehicle.
     * Detectors are used to check for pedestrians in the vehicle's vicinity.
     *
     * @param sec The number of seconds to wait.
     */
    public void waitFor(int sec) {
        timer = sec; // Set the timer for waiting
        waiting = true; // Indicate that the vehicle is in a waiting state
    }

    /**
     * The superclass Vehicle's act() method. This can be called by a Vehicle
     * subclass object (for example, by a Car) in two ways: - If the Vehicle
     * simply does NOT have a method called public void act(), this will be
     * called instead. - subclass' act() method can invoke super.act() to call
     * this, as is demonstrated here.
     */

    public void act() {
        // check if vehicle is waiting (correspond to waitFor method)
        if (waiting) {
            timer--; // Decrease the waiting timer
            if (timer <= 0) {
                speed = maxSpeed; // Reset speed after waiting
                waiting = false; // Indicate that the vehicle is no longer waiting
            }
        }
        
        // If the vehicle is already switching lanes, don't switch lanes anymore
        if (switchingLane) {
            switchLaneCounter--; // Decrease the lane switch counter
            // Move the vehicle in the Y direction based on the switch lane factor
            setLocation(getX(), getY() + switchLaneFactor);
            if (switchLaneCounter <= 0) {
                switchingLane = false; // End switching lane
                // Set the vehicle's location to the origin of the new lane
                setLocation(getX(), world.getOrigin(myLaneNumber).getY() - yOffset);
            }
        } else {
            // Random chance to switch lanes
            if (Math.round(Math.random() * switchlanePossibility) == 1) {
                switchLane(); // Attempt to switch lanes
            }
        }

        if (hitPedestrian > 0) {
            hitPedestrian--; // Countdown hit pedestrian status
            return; // Exit method if currently hitting a pedestrian
        } else {
            drive(); // Move the vehicle forward
            // Check for collision with another vehicle in the specified direction
            if (getFront() && !switchingLane) {
                if (Greenfoot.getRandomNumber(switchlanePossibility/100) == 1) { // if there is a collision, possibility of siwtching lane increase by 100 times
                    switchLane(); // Attempt to switch lanes after collision
                }
            }

            // Check if the vehicle is at the edge of the world
            if (checkEdge()) {
                removeVehicle(); // Remove the vehicle if at the edge
                return;
            }
            // If no pedestrian is hit, repel them away
            if (!checkHitPedestrian()) {
                repelPedestrians(); // Repel pedestrians from the vehicle's path
            } else {
                hitPedestrian = counter; // Set hit pedestrian counter if a hit occurs
            }
        }
    }
    /**
     * 
     * This method checks if there is a vehicle in front of this vehicle
     * Helps vehicle decided weather to switch line or not
     * 
     */
    private boolean getFront() {
        Vehicle v = (Vehicle) getOneObjectAtOffset(direction * (int) speed + getImage().getWidth(), 0, Vehicle.class);
        if (v != null) {
            return true;
        }
        return false;
    }

    /**
     * This method checks for the presence of other vehicles in the 
     * specified direction and returns the object if detected.
     * 
     * @param direction The direction in which to check for collisions.
     * @param clss The class of the objects to check against (e.g., Vehicle).
     * @return The detected object if any; otherwise, null.
     */
    protected Object checkTouchVehicle(int direction, Class<?> clss) {
        int location = (VehicleWorld.getLaneHeight() + VehicleWorld.getLaneSpace()) / 2;
        for (int i = -location; i <= location; i += 4) {
            // Check for an object at the offset position based on direction and speed
            Object p = (Object) getOneObjectAtOffset(direction * (int) speed, i, clss);
            if (p != null) {
                return p; // Return the detected object if found
            }
        }
        return null; // Return null if no object is detected
    }

    /**
     * This method removes the vehicle and its associated detectors from the world.
     * It also removes the vehicle spawner to ensure that no further vehicles are created.
     *
     * This method also adds detectors for the vehicle.
     * Detectors are used to check for pedestrians in the vehicle's vicinity.
     */
    public void removeVehicle() {
        getWorld().removeObject(u); // Remove upper detector
        getWorld().removeObject(d); // Remove lower detector
        getWorld().removeObject(this); // Remove the vehicle itself
    }

    /**
     * A method used by all Vehicles to check if they are at the edge.
     * 
     * Note that this World is set to unbounded (The World's super class is
     * (int, int, int, FALSE) which means that objects should not be stopped
     * from leaving the World. However, this introduces a challenge as there is
     * the potential for objects to disappear off-screen but still be fully
     * acting and thus wasting resources and affecting the simulation even
     * though they are not visible.
     */
    protected boolean checkEdge() {
        if (direction == 1) { // if moving right, check 200 pixels to the right
                                // (above max X)
            if (getX() > getWorld().getWidth() + 200) {
                return true;
            }
        } else { // if moving left, check 200 pixels to the left (negative
                    // values)
            if (getX() < -200) {
                return true;
            }
        }
        return false;
    }

    
    /**
     * This method repels pedestrians (represented as Sheep) that are intersecting with the vehicle.
     * It only considers pedestrians that are awake and ignores knocked-down pedestrians.
     *
     * This method also adds detectors for the vehicle.
     * Detectors are used to check for pedestrians in the vehicle's vicinity.
     */
    public void repelPedestrians() {
        // Get a list of all intersecting Sheep (pedestrians)
        ArrayList<Sheep> pedsTouching = (ArrayList<Sheep>) getIntersectingObjects(Sheep.class);
        
        // Create a new list to hold only awake pedestrians
        ArrayList<Actor> actorsTouching = new ArrayList<Actor>();

        // Filter out knocked-down pedestrians (only add awake ones)
        for (Sheep p : pedsTouching) {
            if (p.isAwake()) {
                actorsTouching.add(p); // Add only awake pedestrians to the list
            }
        }

        // Push the awake pedestrians away from the vehicle by a specified distance
        pushAwayFromObjects(actorsTouching, 4);
    }


    /**
     * New repel method! This method repels nearby pedestrians (represented by Sheep objects)
     * away from the vehicle in the vertical direction (y-axis).
     * It uses a minimum distance to determine how much to push them away.
     * 
     * @param nearbyObjects The list of nearby Actor objects (pedestrians) that may need to be repelled.
     * @param minDistance The minimum distance to maintain between the vehicle and nearby pedestrians.
     * @author Mr Cohen
     */
    public void pushAwayFromObjects(ArrayList<Actor> nearbyObjects, double minDistance) {
        // Get the current position of this actor
        int currentX = getX();
        int currentY = getY();

        // Iterate through the nearby objects
        for (Actor object : nearbyObjects) {
            // Get the position and bounding box of the nearby object
            int objectX = object.getX();
            int objectY = object.getY();
            int objectWidth = object.getImage().getWidth();
            int objectHeight = object.getImage().getHeight();

            // Calculate the distance between this actor and the nearby object's
            // bounding oval
            double distance = Math.sqrt(Math.pow(currentX - objectX, 2) + Math.pow(currentY - objectY, 2));

            // Calculate the effective radii of the bounding ovals
            double thisRadius = Math.max(getImage().getWidth() / 2.0, getImage().getHeight() / 2.0);
            double objectRadius = Math.max(objectWidth / 2.0, objectHeight / 2.0);

            // Check if the distance is less than the sum of the radii
            if (distance < (thisRadius + objectRadius + minDistance)) {
                // Calculate the direction vector from this actor to the nearby
                // object
                int deltaX = objectX - currentX;
                int deltaY = objectY - currentY;

                // Calculate the unit vector in the direction of the nearby
                // object
                double length = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                double unitX = deltaX / length;
                double unitY = deltaY / length;

                // Calculate the amount by which to push the nearby object
                double pushAmount = (thisRadius + objectRadius + minDistance) - distance;

                // Update the position of the nearby object to push it away

                object.setLocation(objectX, objectY + (int) (pushAmount * unitY));

            }
        }
    }

    /**
     * Method that handles the movement of the vehicle.
     * Speed can be set by individual subclasses in their constructors.
     * This method checks for obstacles ahead and adjusts speed accordingly.
     */
    public void drive() {
        // Ahead is a generic vehicle - we don't know what type BUT
        // since every Vehicle "promises" to have a getSpeed() method,
        // we can call that on any vehicle to find out it's speed
        Vehicle ahead = (Vehicle) getOneObjectAtOffset(direction * (int) (speed + getImage().getWidth() / 2 + 6), 0,
                Vehicle.class);
        double otherVehicleSpeed = -1;
        // If a vehicle is found ahead, retrieve its speed
        if (ahead != null) {

            otherVehicleSpeed = ahead.getSpeed();
        }

        // Various things that may slow down driving speed
        // You can ADD ELSE IF options to allow other
        // factors to reduce driving speed.

        // Is vehicle ahead slower?
        if (otherVehicleSpeed >= 0 && otherVehicleSpeed < maxSpeed) {
            speed = otherVehicleSpeed;
        }
        else {
            speed = maxSpeed; // nothing impeding speed, so go max speed
        }

        move(speed * direction);
    }

    /**
     * An accessor method that provides the current speed of this Vehicle.
     * This is useful for scenarios where the vehicle needs to check its speed
     * relative to other vehicles in the same lane.
     *
     * @return the current speed of the vehicle if it is moving; otherwise, returns 0.
     */
    public double getSpeed() {
        // Return the speed if the vehicle is actively moving; otherwise, return 0
        if (moving) {
            return speed; // Active speed
        }
        return 0; // Vehicle is not moving
    }
}
