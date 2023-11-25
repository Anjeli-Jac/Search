/*
 * Copyright (c) 2022 Ian Clement. All rights reserved.
 */

package ca.qc.johnabbott.cs4p6.utility;

import ca.qc.johnabbott.cs4p6.search.*;
import ca.qc.johnabbott.cs4p6.terrain.Generator;
import ca.qc.johnabbott.cs4p6.terrain.Terrain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

/**
 * Represents the app configuration, loaded from properties files in the 'res' folder.
 */
public class Config {
    /**
     * Create a Search object for the algorithm name provided.
     * @param algorithmName
     * @return
     * @throws UnknownAlgorithm
     */
    public static Search getAlgorithmByName(String algorithmName, Terrain terrain) throws UnknownAlgorithm {
        return switch (algorithmName.toUpperCase()) {
            // TODO: uncomment this for DFS
            case "DFS" -> new DFS(terrain);
            // TODO: uncomment this for BFS
            case "BFS" -> new BFS(terrain);
            case "TERRIBLE" -> new TerribleSearch(terrain);
            case "RANDOM" -> new RandomSearch(terrain);
            default -> throw new UnknownAlgorithm("Unknown search algorithm: " + algorithmName +
                    ". You will need add a case to \"getAlgorithmByName(..)\". Follow this stacktrace to see where to add it!");
        };
    }

    // fields

    private Search search;
    private Terrain terrain;
    private boolean animate;
    private int width;
    private int height;
    private double density;
    private int clusters;
    private boolean debug;
    private String player;

    /**
     * Load the config from the properties file.
     * @param propertiesFile
     * @throws PropertiesException
     */
    public Config(File propertiesFile) throws PropertiesException {

        // extract app config from the .properties file
        Properties properties = new Properties();
        try {
            // load global properties first
            properties.load(new FileReader("res/global.properties"));
            // load specific properties second.
            properties.load(new FileReader(propertiesFile));
        } catch (IOException e) {
            throw new PropertiesException(e);
        }

        // parse simple properties
        try {
            animate = Boolean.valueOf(properties.getProperty("animate"));
            width = Integer.parseInt(properties.getProperty("width"));
            height = Integer.parseInt(properties.getProperty("height"));
            density = Double.parseDouble(properties.getProperty("density"));
            clusters = Integer.parseInt(properties.getProperty("clusters"));
            debug = Boolean.valueOf(properties.getProperty("debug"));
            player = properties.getProperty("player");
        }
        catch (NumberFormatException e) {
            throw new PropertiesException(e);
        }

        // load terrain
        if (properties.getProperty("terrain").equals("random")) {
            Generator<Terrain> generator = Terrain.generator(width, height, density, clusters);
            terrain = generator.generate(new Random());
        }
        else {
            try {
                terrain = new Terrain(properties.getProperty("terrain"));
            } catch (FileNotFoundException e) {
                throw new PropertiesException(e);
            }
        }

        String algorithmName = properties.getProperty("search");
        if(algorithmName == null)
            throw new PropertiesException("Missing search property");

        try {
            search = getAlgorithmByName(algorithmName, terrain);
            if(debug)
                search.turnOnVerbose();
        } catch (UnknownAlgorithm e) {
            throw new PropertiesException(e);
        }

    }

    public Search getSearch() {
        return search;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public boolean animate() {
        return animate;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getDensity() {
        return density;
    }

    public int getClusters() {
        return clusters;
    }

    public boolean debug() {
        return debug;
    }

    public String getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "Config{" +
                "search=" + search +
                ", terrain='" + terrain + '\'' +
                ", animate=" + animate +
                ", width=" + width +
                ", height=" + height +
                ", density=" + density +
                ", clusters=" + clusters +
                '}';
    }


}
