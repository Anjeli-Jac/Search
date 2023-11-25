/*
 * Copyright (c) 2022 Ian Clement. All rights reserved.
 */

package ca.qc.johnabbott.cs4p6.graphics;

import ca.qc.johnabbott.cs4p6.collections.Traversable;
import ca.qc.johnabbott.cs4p6.collections.Tuple2;
import ca.qc.johnabbott.cs4p6.graphics.animation.*;
import ca.qc.johnabbott.cs4p6.graphics.drawable.*;
import ca.qc.johnabbott.cs4p6.search.Color;
import ca.qc.johnabbott.cs4p6.search.Search;
import ca.qc.johnabbott.cs4p6.terrain.Direction;
import ca.qc.johnabbott.cs4p6.terrain.Location;
import ca.qc.johnabbott.cs4p6.terrain.Terrain;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Create an animated solution to the search problem
 *
 * @author Ian Clement (ian.clement@johnabbott.qa.ca)
 */
public class GraphicalTerrain {

    // the canvas displays the solution
    private Canvas canvas;

    // the terrain with the solution set
    private Terrain terrain;

    /**
     * Construct and run the animated solution
     *
     * @param terrain
     */
    private GraphicalTerrain(Terrain terrain, Search search, Traversable<Direction> path, int width, int height, boolean doColors, String player) {
        this.terrain = terrain;

        TerrainDrawable.setCellSize(Math.min(width / (terrain.getWidth() + 2), height / (terrain.getHeight() + 2)));

        PlayerSprite playerAnimation = switch (player) {
            case "vik" -> VikSprite.getInstance();
            case "ian" -> IanSprite.getInstance();
            default -> throw new IllegalArgumentException("No animation for player: " + player);
        };
        IanSprite.getInstance();
        CoffeeSprite coffeeAnimation = CoffeeSprite.getInstance();
        LandSprite landAnimation = LandSprite.getInstance();

        Animated startAnimation = new Constant(coffeeAnimation.getSpriteSheet().getSheet(3, 0));
        Animated goalAnimation = new Loop(coffeeAnimation.getFullCoffeeAnimation());

        Drawable floor = landAnimation.getSpriteSheet().getSheet(0, 0);
        Drawable wall = landAnimation.getSpriteSheet().getSheet(0, 1);

        // initalize all drawables: the terrain with familiar graphics!
        List<Drawable> drawables = new LinkedList<>();

        TerrainDrawable terrainDrawable = new TerrainDrawable(terrain, startAnimation, goalAnimation, wall, floor);
        //terrainDrawable.setColors(search.getColors());
        drawables.add(terrainDrawable);

        List<Animated> animations = new LinkedList<>();

        animations.add(startAnimation);
        animations.add(goalAnimation);

        Location goal = terrain.getGoal();

        // follow the solution path from the start to the goal
        Location current = terrain.getStart();
        int limit = terrain.getHeight() * terrain.getWidth();

        List<Tuple2<Location, Color>> colorHistory = search.getColorHistory();

        Sequence seq = new Sequence(terrain.getWidth() * terrain.getHeight() + colorHistory.size());

        for(int i=1; i <= colorHistory.size(); i++)
            seq.add(new Constant(70, new ColorLayerDrawable(terrain, colorHistory, i)));



        path.reset();
        while (!current.equals(goal) && limit >= 0 && path.hasNext()) {
            limit--;

            // get the fromDirection and the position to get to
            Direction to = path.next();

            if (to == null || to == Direction.NONE)
                break;

            Location next = current.get(to);

            // ignore any bad move.
            if(!terrain.inTerrain(next) || terrain.isWall(next))
                continue;

            // add a new Step graphics from the current position to the next position
            Animated animation = null;
            switch (to) {
                case UP:
                    animation = playerAnimation.getMoveUpAnimation();
                    break;
                case DOWN:
                    animation = playerAnimation.getMoveDownAnimation();
                    break;
                case RIGHT:
                    animation = playerAnimation.getMoveRightAnimation();
                    break;
                case LEFT:
                    animation = playerAnimation.getMoveLeftAnimation();
                    break;
            }
            seq.add(new Step(current, next, 250, animation));

            // step to the next position
            current = next;
        }

        if (terrain.isSolved())
            seq.add(new Loop(playerAnimation.getVictoryAnimation(terrain.getGoal().getX() * TerrainDrawable.getCellSize(), terrain.getGoal().getY() * TerrainDrawable.getCellSize())));
        else
            seq.add(playerAnimation.getDefeatAnimation(current.getX() * TerrainDrawable.getCellSize(), current.getY() * TerrainDrawable.getCellSize()));

        // add the movement sequence to the animations and the drawables
        animations.add(seq);
        drawables.add(seq);

        // create a Swing frame to contain the solution canvas
        JFrame frame = new JFrame();
        frame.setTitle("Explorer!");
        frame.setBounds(0, 0, width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // initialize the canvas with the drawables and animations.
        canvas = new Canvas(drawables, animations);

        // include the canvas in the frame and show to the user
        canvas.setFocusable(true);
        frame.add(canvas);
        frame.setVisible(true);
    }

    /**
     * Run the animated solution using the solution contained in the provided terrain.
     *
     * @param terrain contains the solution.
     */
    public static void run(Terrain terrain, int width, int height, boolean doColors, String player) {
        new GraphicalTerrain(terrain, null, null, width, height, doColors, player);
    }

    /**
     * Run the animated solution using the solution contained in the provided terrain.
     *
     * @param terrain contains the solution.
     */
    public static void run(Terrain terrain, Search search, Traversable<Direction> path, int width, int height, boolean doColors, String player) {
        new GraphicalTerrain(terrain, search, path, width, height, doColors, player);
    }

}
