/*
 * Copyright (c) 2023 Ian Clement. All rights reserved.
 */

package ca.qc.johnabbott.cs4p6.search;

import ca.qc.johnabbott.cs4p6.collections.Queue;
import ca.qc.johnabbott.cs4p6.collections.SparseArray;
import ca.qc.johnabbott.cs4p6.collections.Stack;
import ca.qc.johnabbott.cs4p6.collections.Traversable;
import ca.qc.johnabbott.cs4p6.terrain.Direction;
import ca.qc.johnabbott.cs4p6.terrain.Location;
import ca.qc.johnabbott.cs4p6.terrain.Terrain;

public class DFS extends Search{

    private SparseArray<Direction> fromDirections; // get the direction of where the nextLocation is coming from
    private SparseArray<Direction> toDirections;  // records to where the currentLocation is going towards
    private Stack<Direction> pathDirections; //keeps track of the LIFO directions of the path

    private final int height;
    private final int width;

    /**
     * Construct The DFS search.
     * @param terrain The terrain to perform the search on.
     */
    public DFS(Terrain terrain) {
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
        pathDirections = new Stack<>(getSizeOfTerrain());

        // get clockwise direction array.
        Direction[] clockwise = Direction.getClockwise();

        // track the current search location, starting at the terrain start location.
        Location currentLocation = start;
        setColorAtLocation(start, Color.BLACK);

        // starting from the up direction
        Direction currentDirection= Direction.UP;

        int urdl_tracker=0;
        int iteration = 0;

        // main loop of algorithm
        while(!currentLocation.equals(terrain.getGoal())) {

            if(inVerboseMode()) {
                System.out.println("==========================================");
                System.out.println("Iteration: " + iteration++);
            }

            Location nextLocation = currentLocation.get(currentDirection);

            // change direction if we can't go in the previous direction,
            boolean invalidLocation = !terrain.inTerrain(nextLocation)
                    || terrain.isWall(nextLocation)
                    || colorAt(nextLocation) != Color.WHITE;

            if(invalidLocation) {

                urdl_tracker++;
                // get a new direction and start a new iteration
                if(urdl_tracker<clockwise.length){
                    currentDirection = clockwise[urdl_tracker];
                }
                else{
                    //exits the while loop if backtracking goes all the way back to the initial position
                    // and all sides have been already visited
                    if((currentLocation.equals(terrain.getStart())) && currentDirection==Direction.LEFT){
                        break;
                    }

                    //saves the last direction it stepped into
                    currentDirection= pathDirections.top();
                    pathDirections.pop();

                    // step *moves backwards*
                    currentLocation=currentLocation.get(currentDirection);

                    //finds the next direction to check from the last
                    for(int counter=0; counter<clockwise.length;counter++){
                        if(currentDirection.opposite()==clockwise[counter]) {
                            //the next direction
                            int index=counter+1;
                            //makes sure that there is no other direction after left
                            if(index<clockwise.length){
                                currentDirection = clockwise[index];
                                //resets the tracker to current direction
                                urdl_tracker=index;
                                break;
                            }
                        }
                    }
                }
                if (inVerboseMode())
                    System.out.println("Changing direction: " + currentDirection);
                continue;

            }


            // record the step we've taken to memory to recreate the solution in the later traversal.
            toDirections.set(currentLocation, currentDirection);
            fromDirections.set(nextLocation, currentDirection.opposite());
            pathDirections.push(currentDirection.opposite());

            // step
            currentLocation= currentLocation.get(currentDirection);

            // record that we've new seen this location
            setColorAtLocation(currentLocation, Color.BLACK);

            //reset current direction to the initial point
            urdl_tracker=0;
            currentDirection=clockwise[urdl_tracker];

            // output that could be useful for debugging.
            if(inVerboseMode()) {
                System.out.println("To directions:");
                System.out.println(toDirections);
                System.out.println("From directions:");
                System.out.println((fromDirections));
            }
        }

        //creates the path and returns
        Queue<Direction> path = new Queue<>(iteration);
        Location solutionCursor = terrain.getStart();
        while (!solutionCursor.equals(terrain.getGoal())) {
            Direction next = toDirections.get(solutionCursor);
            if (next == Direction.NONE)
                break;
            solutionCursor = solutionCursor.get(next);
            path.enqueue(next);
        }
        return path;
    }

    @Override
    public String toString() {
        return "DFS Search";
    }
}