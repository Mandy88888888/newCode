import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class DayMode here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DayMode extends VehicleWorld
{

    /**
     * Constructor for objects of class DayMode.
     * 
     */
    public DayMode()
    {
        super();
        image = new GreenfootImage("download.png");
        prepareLanes (this, image, laneSpawners, 400-20, laneHeight, laneCount, spaceBetweenLanes, twoWayTraffic, splitAtCenter, new Color (88, 88, 88));
        setBackground(image);
    }

    public void act () {
        super.act();
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
                    addObject(new Bus(laneSpawners[lane]), 0, 0);
                } else if (vehicleType == 2){
                    addObject(new Ambulance(laneSpawners[lane]), 0, 0);
                }
            }
        }

        // Chance to spawn a Sheep
        if (Greenfoot.getRandomNumber (60) == 0){
            int xSpawnLocation = Greenfoot.getRandomNumber (600) + 100; // random between 99 and 699, so not near edges
            boolean spawnAtTop = Greenfoot.getRandomNumber(2) == 0 ? true : false;
            if (spawnAtTop){

                int choice = (int) Math.round(Math.random()*3) + 1;
                switch (choice) {
                    case 1:
                        addObject (new SmartSheep (1), xSpawnLocation, TOP_SPAWN);
                        break;
                    case 2:
                        addObject (new CuteSheep (1), xSpawnLocation, TOP_SPAWN);
                        break;

                    case 3:
                        addObject (new SlowSheep (1), xSpawnLocation, TOP_SPAWN);
                        break;
                    case 4:

                        addObject (new PrettySheep (1), xSpawnLocation, TOP_SPAWN);

                        break;
                }
            } else {

                int choice = (int) Math.round(Math.random()*3) + 1;
                switch (choice) {
                    case 1:
                        addObject (new SmartSheep (-1), xSpawnLocation, BOTTOM_SPAWN);
                        break;
                    case 2:
                        addObject (new CuteSheep (-1), xSpawnLocation, BOTTOM_SPAWN);
                        break;

                    case 3:
                        addObject (new SlowSheep (-1), xSpawnLocation, BOTTOM_SPAWN);

                        //only come out during the day
                        break;
                    case 4:

                        addObject (new PrettySheep (-1), xSpawnLocation, BOTTOM_SPAWN);

                        break;
                }
            }
        }
    }
}

    
