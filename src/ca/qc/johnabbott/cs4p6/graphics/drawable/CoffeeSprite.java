/*
 * Copyright (c) 2022 Ian Clement. All rights reserved.
 */

package ca.qc.johnabbott.cs4p6.graphics.drawable;

import ca.qc.johnabbott.cs4p6.graphics.animation.Animated;

import javax.swing.*;
import java.awt.*;

/**
 * Sprite singleton class for "coffee.png"
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public class CoffeeSprite {

    private static final String COFFEE_FILE = "res/assets/coffee.png";
    private static CoffeeSprite instance;

    public static CoffeeSprite getInstance() {
        if (instance == null)
            instance = new CoffeeSprite();
        return instance;
    }

    private final Animated fullCoffeeAnimation;
    private final SpriteSheet sheet;

    private CoffeeSprite() {
        Image image = (new ImageIcon(COFFEE_FILE)).getImage();
        sheet = new SpriteSheet(image, 32, 32);

        fullCoffeeAnimation = sheet.getBuilder(3, 0, 0, TerrainDrawable.getCellSize(), TerrainDrawable.getCellSize())
                .add(0, 0, 1000f / 3)
                .add(1, 0, 1000f / 3)
                .add(2, 0, 1000f / 3)
                .build();
    }

    // get the
    public Animated getFullCoffeeAnimation() {
        return fullCoffeeAnimation;
    }

    public SpriteSheet getSpriteSheet() {
        return sheet;
    }
}
