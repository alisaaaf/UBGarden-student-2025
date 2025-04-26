package fr.ubx.poo.ubgarden.game.launcher;

import fr.ubx.poo.ubgarden.game.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class GameLauncher {

    private GameLauncher() {
    }

    public static GameLauncher getInstance() {
        return LoadSingleton.INSTANCE;
    }

    private int integerProperty(Properties properties, String name, int defaultValue) {
        return Integer.parseInt(properties.getProperty(name, Integer.toString(defaultValue)));
    }

    private Configuration getConfiguration(Properties properties) {
        int waspMoveFrequency = integerProperty(properties, "waspMoveFrequency", 2);
        int hornetMoveFrequency = integerProperty(properties, "hornetMoveFrequency", 1);
        int gardenerEnergy = integerProperty(properties, "gardenerEnergy", 100);
        int energyBoost = integerProperty(properties, "energyBoost", 50);
        long energyRecoverDuration = integerProperty(properties, "energyRecoverDuration", 1000);
        long diseaseDuration = integerProperty(properties, "diseaseDuration", 5000);

        return new Configuration(gardenerEnergy, energyBoost, energyRecoverDuration, diseaseDuration,
                waspMoveFrequency, hornetMoveFrequency);
    }

    public Game load() {
        Properties emptyConfig = new Properties();
        MapLevel mapLevel = new MapLevelDefaultStart();
        Position gardenerPosition = mapLevel.getGardenerPosition();
        if (gardenerPosition == null)
            throw new RuntimeException("Gardener not found");
        Configuration configuration = getConfiguration(emptyConfig);
        World world = new World(1);
        Game game = new Game(world, configuration, gardenerPosition);
        Map level = new Level(game, 1, mapLevel);
        world.put(1, level);

        game.initCarrots();

        return game;
    }


    public Game loadFromFile(File file) {
        try {
            MapLevel mapLevel = new MapLevelFromFile(file);
            Configuration config = getConfiguration(new Properties());
            World world = new World(1);
            Position gardenerPos = mapLevel.getGardenerPosition();
            if (gardenerPos == null)
                throw new RuntimeException("Gardener not found");
            Game game = new Game(world, config, gardenerPos);
            Map level = new Level(game, 1, mapLevel);
            world.put(1, level);

            game.initCarrots();

            return game;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load map from file", e);
        }
    }


    private static class LoadSingleton {
        static final GameLauncher INSTANCE = new GameLauncher();
    }
}
