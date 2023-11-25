/*
 * Copyright (c) 2022 Ian Clement. All rights reserved.
 */

package ca.qc.johnabbott.cs4p6.graphics.drawable;

import ca.qc.johnabbott.cs4p6.graphics.animation.Animated;

import javax.swing.*;
import java.awt.*;

/**
 * Sprite singleton class for "vik.png"
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public class VikSprite extends PlayerSprite {

    private static final String VIK_FILE = "res/assets/vik-2023.png";
    private static VikSprite instance;

    public static VikSprite getInstance() {
        if (instance == null)
            instance = new VikSprite();
        return instance;
    }

    public VikSprite() {
        super(VIK_FILE);
    }
}
