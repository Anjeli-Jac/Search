/*
 * Copyright (c) 2022 Ian Clement. All rights reserved.
 */

package ca.qc.johnabbott.cs4p6.graphics.drawable;

import ca.qc.johnabbott.cs4p6.collections.Tuple2;
import ca.qc.johnabbott.cs4p6.terrain.Location;
import ca.qc.johnabbott.cs4p6.terrain.Terrain;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Draw colored layer of search algorithm.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public class ColorLayerDrawable implements Drawable {

    private static final Color BLACK;
    private static final Color GREY;

    static {
        BLACK = new Color(0,0,0,150);
        GREY = new Color(100,100,100,150);
    }


    private final int limit;
    private Terrain terrain;
    private List<Tuple2<Location, ca.qc.johnabbott.cs4p6.search.Color>> colorsHistory;
    private JComponent observer;

    public ColorLayerDrawable(Terrain terrain, List<Tuple2<Location, ca.qc.johnabbott.cs4p6.search.Color>> colorsHistory) {
        this(terrain, colorsHistory, colorsHistory.size());
    }

    public ColorLayerDrawable(Terrain terrain, List<Tuple2<Location, ca.qc.johnabbott.cs4p6.search.Color>> colorsHistory, int limit) {
        this.terrain = terrain;
        this.colorsHistory = colorsHistory;
        this.limit = limit;
    }

    @Override
    public void setObserver(JComponent observer) {
        this.observer = observer;
    }

    @Override
    public void draw(Graphics g, int offsetX, int offsetY) {

        Graphics2D graphics2D = (Graphics2D) g;

        int cellSize = TerrainDrawable.getCellSize();

        int count = 0;
        for (Tuple2<Location, ca.qc.johnabbott.cs4p6.search.Color> tuple : colorsHistory) {

            if (count++ == limit)
                break;

            Location current = tuple.getFirst();

            if (terrain.inTerrain(current) && !terrain.isWall(current)) {
                switch (tuple.getSecond()) {
                    case GREY:
                        graphics2D.setColor(GREY);
                        graphics2D.fillRect(offsetX + current.getX() * cellSize, offsetY + current.getY() * cellSize, cellSize, cellSize);
                        break;
                    case BLACK:
                        graphics2D.setColor(BLACK);
                        graphics2D.fillRect(offsetX + current.getX() * cellSize, offsetY + current.getY() * cellSize, cellSize, cellSize);
                        break;
                    case WHITE:
                        break;
                }
            }
        }
    }
}
