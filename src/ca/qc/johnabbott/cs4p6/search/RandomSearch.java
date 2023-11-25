/*
 * Copyright (c) 2022 Ian Clement. All rights reserved.
 */

package ca.qc.johnabbott.cs4p6.search;

import ca.qc.johnabbott.cs4p6.collections.Queue;
import ca.qc.johnabbott.cs4p6.collections.SparseArray;
import ca.qc.johnabbott.cs4p6.collections.Traversable;
import ca.qc.johnabbott.cs4p6.terrain.*;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * A "search" for the goal, choosing a random direction each step (but favouring the previous direction 3/4 of the time).
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public class RandomSearch extends Search {

    private static final int MAX = 15;

    // records where we've been and what steps we've taken.
    private SparseArray<Direction> fromDirections;  // not useful in this algorithm...
    private SparseArray<Direction> toDirections;


    /**
     * Create a new Random search.
     */
    public RandomSearch(Terrain terrain) {
        super(terrain);
    }


    @Override
    public Traversable<Direction> solve(Location start) {
        super.solve(start);

        // track locations we've been to be using our terrain "memory"
        fromDirections = new SparseArray<>(Direction.NONE);
        toDirections = new SparseArray<>(Direction.NONE);

        // setup random direction generator.
        Random random = new Random();
        Generator<Direction> generator = Direction.generator();

        // track the current search location, starting at the terrain start location.
        Location currentLocation = start;
        setColorAtLocation(start, Color.BLACK);

        // start in a random direction. We will adjust this accordingly.
        Direction currentDirection = generator.generate(random);

        int iteration = 0;

        // in random search we'll try different random directions up to MAX times before giving up.
        int stuckCount = 0;

        // main loop of algorithm
        while(!currentLocation.equals(terrain.getGoal()) &&stuckCount < MAX ) {

            if(inVerboseMode()) {
                System.out.println("==========================================");
                System.out.println("Iteration: " + iteration++);
            }

            Location nextLocation = currentLocation.get(currentDirection);

            // change direction if we can't go in the previous direction,
            boolean invalidLocation = !terrain.inTerrain(nextLocation)
                    || terrain.isWall(nextLocation)
                    || colorAt(nextLocation) != Color.WHITE;

            if (invalidLocation)
                stuckCount++;

            //  25% chance of changing direction anyway
            boolean changeDirection = random.nextInt(100) < 25;

            if(invalidLocation || changeDirection) {
                // get a new direction and start a new iteration
                currentDirection = generator.generate(random);
                if (inVerboseMode())
                    System.out.println("Changing direction: " + currentDirection);
                continue;
            }

            // record the step we've taken to memory to recreate the solution in the later traversal.
            toDirections.set(currentLocation, currentDirection);

            // step
            currentLocation = currentLocation.get(currentDirection);

            // record that we've new seen this location
            setColorAtLocation(currentLocation, Color.BLACK);

            // output that could be useful for debugging.
            if(inVerboseMode()) {
                System.out.println("To directions:");
                System.out.println(toDirections);
            }
        }

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
        return "Random Search";
    }
}
