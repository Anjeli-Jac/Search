/*
 * Copyright (c) 2022 Ian Clement. All rights reserved.
 */

package ca.qc.johnabbott.cs4p6.graphics.drawable;

import javax.swing.*;
import java.awt.*;

/**
 * Drawable interface
 * @author Ian Clement (ian.clement@johnabbott.qa.ca)
 */
public interface Drawable {
    /**
     * Set the observer for the drawable.
     * @param observer
     */
    void setObserver(JComponent observer);

    /**
     * Draws the drawable.
     * @param g the graphics used to draw the drawable.
     */
    void draw(Graphics g, int offsetX, int offsetY);
}
