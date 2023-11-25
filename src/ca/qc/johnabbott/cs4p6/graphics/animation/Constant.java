/*
 * Copyright (c) 2022 Ian Clement. All rights reserved.
 */

package ca.qc.johnabbott.cs4p6.graphics.animation;

import ca.qc.johnabbott.cs4p6.graphics.drawable.Drawable;

import javax.swing.*;
import java.awt.*;

/**
 * Create a constant animation out of a drawable.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public class Constant implements Animated {

    private int time;
    private boolean forever;
    private Drawable drawable;

    public Constant(Drawable drawable) {
        forever = true;
        this.drawable = drawable;
    }

    public Constant(int time, Drawable drawable) {
        this.time = time;
        forever = false;
        this.drawable = drawable;
    }

    @Override
    public void start() {
    }

    @Override
    public boolean isDone() {
        return !forever && time < 0 ;
    }

    @Override
    public void animate(int time) {
        if(!forever)
            this.time -= time;
    }

    @Override
    public void setObserver(JComponent observer) {
        drawable.setObserver(observer);
    }

    @Override
    public void draw(Graphics g, int offsetX, int offsetY) {
        drawable.draw(g, offsetX, offsetY);
    }
}
