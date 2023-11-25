/*
 * Copyright (c) 2022 Ian Clement. All rights reserved.
 */

package ca.qc.johnabbott.cs4p6.graphics.drawable;

import ca.qc.johnabbott.cs4p6.graphics.animation.Animated;

import javax.swing.*;
import java.awt.*;

/**
 * Sprite singleton class for "ian.png"
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public class IanSprite extends PlayerSprite {

    private static final String IAN_FILE = "res/assets/ian-2023.png";
    private static IanSprite instance;

    public static IanSprite getInstance() {
        if (instance == null)
            instance = new IanSprite();
        return instance;
    }

    public IanSprite() {
        super(IAN_FILE);
    }
}
