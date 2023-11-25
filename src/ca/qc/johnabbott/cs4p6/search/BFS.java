/*
 * Copyright (c) 2023 Ian Clement. All rights reserved.
 */

package ca.qc.johnabbott.cs4p6.search;

import ca.qc.johnabbott.cs4p6.collections.*;
import ca.qc.johnabbott.cs4p6.terrain.Direction;
import ca.qc.johnabbott.cs4p6.terrain.Location;
import ca.qc.johnabbott.cs4p6.terrain.Terrain;


public class BFS extends Search{

    // records where we've been and what steps we've taken.
    private SparseArray<Direction> fromDirections;  // get the direction of where the nextLocation is coming from
    private SparseArray<Direction> toDirections;    // records to where the currentLocation is going towards
    private Queue<Tuple2<Location,Direction>> orderToCheck; //keeps track of the search order
    private Stack<Direction> directions; //keeps track of the LIFO directions of the path

    private final int height;
    private final int width;
    /**
     * Construct The BFS search.
     * @param terrain The terrain to perform the search on.
     */
    public BFS(Terrain terrain) {
        super(terrain);
        height=terrain.getHeight();
        width= terrain.getWidth();
    }

    //finds the number of squares in the area
    private int getSizeOfTerrain(){
        return  this.height*this.width;
    }

    /**
     * Find a solution path, if one exists, from the provided start location.
     * @param start The provided start location.
     * @return The solution path in a traversable structure.
     */
    @Override
    public Traversable<Direction> solve(Location start) {
        super.solve(start);

        // track locations we've been to be using our terrain "memory"
        fromDirections = new SparseArray<>(Direction.NONE);
        toDirections = new SparseArray<>(Direction.NONE);
        orderToCheck = new Queue<>(getSizeOfTerrain());

        // track the current search location, starting at the terrain start location.
        Location currentLocation = start;
        setColorAtLocation(start, Color.BLACK);

        // get clockwise direction array.
        Direction[] clockwise = Direction.getClockwise();
        // starting from the up direction
        Direction currentDirection;

        int iteration = 0;

        //Finds the first set of directions that the currentLocation can go to, and adds it to the queue
        for (Direction value : clockwise) {
            currentDirection = value;
            Location nextLocation = currentLocation.get(currentDirection);

            boolean invalidLocation = !terrain.inTerrain(nextLocation)
                    || terrain.isWall(nextLocation)
                    || colorAt(nextLocation) != Color.WHITE;

            if (!invalidLocation) {
                setColorAtLocation(nextLocation, Color.GREY);
                Tuple2 position = new Tuple2(currentLocation, value);
                orderToCheck.enqueue(position);
                toDirections.set(currentLocation, currentDirection);
                fromDirections.set(currentLocation, currentDirection.opposite());

            }
        }

        // main loop of algorithm
        while(!currentLocation.equals(terrain.getGoal())){

            //exits once there is no more values to evaluate form the queue
            if(orderToCheck.isEmpty())
            {
                break;
            }

            //gets the location and direction on the first positioned object in the queue
            Tuple2 pos=orderToCheck.dequeue();
            currentLocation= (Location) pos.getFirst();
            currentDirection= (Direction) pos.getSecond();


            Location nextLocation = currentLocation.get(currentDirection);
            fromDirections.set(nextLocation, currentDirection.opposite());

            //step into
            currentLocation=nextLocation;
            // record that we've new visited this location
            setColorAtLocation(currentLocation,Color.BLACK);

            //checks for all valid directions that th currentLocation can go to
            for (Direction direction : clockwise) {

                if (inVerboseMode()) {
                    System.out.println("==========================================");
                    System.out.println("Iteration: " + iteration++);
                }
                currentDirection = direction;
                nextLocation = currentLocation.get(currentDirection);

                boolean invalidLocation = !terrain.inTerrain(nextLocation)
                        || terrain.isWall(nextLocation)
                        || colorAt(nextLocation) != Color.WHITE;

                //adds the seen cells fom currentLocation to the queue that are valid,
                // or else, it moves on to the next direction
                if (!invalidLocation) {
                    // record that we've new visited this location
                    setColorAtLocation(nextLocation, Color.GREY);

                    //creates the new position to be checked and adds it to the queue
                    Tuple2 position = new Tuple2(currentLocation, direction);
                    orderToCheck.enqueue(position);
                    toDirections.set(currentLocation, currentDirection);

                }
            }
        }
        // output that could be useful for debugging.
        if (inVerboseMode()) {
            System.out.println("To directions:");
            System.out.println(toDirections);
            System.out.println("From directions:");
            System.out.println((fromDirections));
        }

        directions= new Stack<>(getSizeOfTerrain());
        Queue<Direction> path = new Queue<>(iteration);
        Location solutionCursor1 = terrain.getGoal();

        //Finds the path starting from the goal position to the start
        while (!solutionCursor1.equals(terrain.getStart())) {
            //uses fromDirection because this shows path of where the goal is attained
            Direction next = fromDirections.get(solutionCursor1);
            if (next == Direction.NONE)
                break;
            solutionCursor1 = solutionCursor1.get(next);
            directions.push(next.opposite()); //adds the opposite direction to the stack because of the reverse order

        }
        //adds the found directions to the path queue and returns to be executed
        while(!directions.isEmpty()){
            path.enqueue(directions.pop());
        }
        return path;
    }

    @Override
    public String toString() {
        return "BFS Search";
    }
}


