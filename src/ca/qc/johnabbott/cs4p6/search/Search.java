/*
 * Copyright (c) 2022 Ian Clement. All rights reserved.
 */

package ca.qc.johnabbott.cs4p6.search;

import ca.qc.johnabbott.cs4p6.collections.*;
import ca.qc.johnabbott.cs4p6.terrain.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Search superclass. Structures the search algorithms and implements search color logic.
 *
 * @author Ian Clement (ian.clement@johnabbott.qa.ca)
 */
public class Search {

    // the terrain we're searching in.
    protected Terrain terrain;

    private boolean verboseMode;

    // for tracking the color information
    private SparseArray<Color> colors;
    private List<Tuple2<Location, Color>> colorHistory;

    // for tracking solution information
    private boolean solutionFound;

    /**
     * Construct a search.
     * @param terrain The terrain to perform the search on.
     */
    public Search(Terrain terrain) {
        this(terrain, false);
    }

    /**
     * Construct a search (verbose mode).
     * @param terrain The terrain to perform the search on.
     * @param verboseMode To turn on verbose mode.
     */
    public Search(Terrain terrain, boolean verboseMode) {
        this.terrain = terrain;
        this.verboseMode = verboseMode;
        solutionFound = false;
    }

    /**
     * Clear all the search colors and clear the history of search color modification.
     */
    protected void clearAllColors() {
        colors = new SparseArray<>(Color.WHITE);
        colorHistory = new LinkedList<>();
    }

    /**
     * Set the search color at a location. In verbose mode this will print out the colors.
     * @param location The location. This location must be within the search terrain.
     * @param color The color.
     */
    protected void setColorAtLocation(Location location, Color color) {

        // check that we are within bounds.
        if(!terrain.inTerrain(location))
            throw new TerrainBoundsException();

        // ignore out of terrain or repeated coloring
        if (colors.get(location) == color)
            return;

        // record the coloring
        colors.set(location, color);
        colorHistory.add(new Tuple2<>(location, color));

        // if we have colored the goal we have found the solution.
        if (location.equals(terrain.getGoal()))
            solutionFound = true;

        if(verboseMode)
            System.out.println(colors);
    }

    /**
     * Get the history of search color modification as a list.
     * @return The list of search color modifications.
     */
    public List<Tuple2<Location, Color>> getColorHistory() {
        return colorHistory;
    }

    /**
     * Get the search color at location.
     * @param location The location.
     * @return The search color at the location.
     */
    protected Color colorAt(Location location) {
        return colors.get(location);
    }

    /**
     * Find a solution path, if one exists, from the provided start location.
     * @param start The provided start location.
     * @return The solution path in a traversable structure.
     */
    public Traversable<Direction> solve(Location start) {
        clearAllColors();
        solutionFound = false;
        return new Queue<>();
    }

    /**
     * Determine if the last search actually found a solution.
     * @return
     */
    public final boolean lastSearchFoundSolution() {
        return solutionFound;
    }

    /**
     * Determine if the search is in verbose mode.
     * @return
     */
    public boolean inVerboseMode() {
        return verboseMode;
    }

    /**
     * Set the search to verbose mode, where it will print out debug messages.
     */
    public void turnOnVerbose() {
        verboseMode = true;
    }

}
