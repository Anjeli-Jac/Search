/*
 * Copyright (c) 2023 Ian Clement. All rights reserved.
 */

package ca.qc.johnabbott.cs4p6.search;

import ca.qc.johnabbott.cs4p6.collections.Queue;
import ca.qc.johnabbott.cs4p6.collections.Traversable;
import ca.qc.johnabbott.cs4p6.terrain.Direction;
import ca.qc.johnabbott.cs4p6.terrain.Location;
import ca.qc.johnabbott.cs4p6.terrain.Terrain;

import java.util.Random;

public class TerribleSearch extends Search {

    public TerribleSearch(Terrain terrain) {
        super(terrain);
    }

    @Override
    public Traversable<Direction> solve(Location start) {
        super.solve(start);

        // Create a random queue of size in [20,100]
        Random random = new Random();
        int pathLength = random.nextInt(80) + 20;

        Direction[] direction = Direction.getClockwise();

        // Fill a queue with random directions.
        // There is no consideration for cells we've seen before: the path will walk back over previous cells.
        // It's a terrible search strategy!
        Queue<Direction> path = new Queue<>(pathLength);
        for(int i=0; i<pathLength; i++)
            path.enqueue(direction[random.nextInt(direction.length)]);

        return path;
    }
}
