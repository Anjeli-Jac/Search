/*
 * Copyright (c) 2022 Ian Clement. All rights reserved.
 */

package ca.qc.johnabbott.cs4p6.graphics.drawable;

import javax.swing.*;
import java.awt.*;

/**
 * Sprite singleton class for "land.png"
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public class LandSprite {

    private static final String LAND_FILE = "res/assets/land.png";
    private static LandSprite instance;
    private final SpriteSheet sheet;

    public static LandSprite getInstance() {
        if (instance == null)
            instance = new LandSprite();
        return instance;
    }

    private LandSprite() {
        Image image = (new ImageIcon(LAND_FILE)).getImage();
        sheet = new SpriteSheet(image, 32, 32);
    }

    public SpriteSheet getSpriteSheet() {
        return sheet;
    }
}