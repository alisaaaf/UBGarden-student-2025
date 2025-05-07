package fr.ubx.poo.ubgarden.game.launcher;

import fr.ubx.poo.ubgarden.game.*;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.decor.special.ClosedDoor;

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
        return loadMultiLevel();
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

    public Game loadMultiLevel() {
        Properties emptyConfig = new Properties();
        Configuration configuration = getConfiguration(emptyConfig);
        World world = new World(2); // 2 niveaux

        // Niveau 1
        MapLevel mapLevel1 = new MapLevelDefaultStart();
        Position gardenerPosition = mapLevel1.getGardenerPosition();
        Game game = new Game(world, configuration, gardenerPosition);

        // Important: créer le niveau 1 avec la bonne porte
        Level level1 = new Level(game, 1, mapLevel1) {
            @Override
            protected Decor createDecor(Position position, MapEntity mapEntity) {
                if (mapEntity == MapEntity.ClosedDoor) {
                    return new ClosedDoor(position, 2); // Bien spécifier le niveau cible
                }
                return super.createDecor(position, mapEntity);
            }
        };
        world.put(1, level1);

        // Niveau 2
        MapLevel mapLevel2 = new MapLevelSecondGarden();
        Level level2 = new Level(game, 2, mapLevel2);
        world.put(2, level2);

        game.initCarrots();
        return game;
    }

}
