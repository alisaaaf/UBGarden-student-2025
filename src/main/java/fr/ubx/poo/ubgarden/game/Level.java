package fr.ubx.poo.ubgarden.game;

import fr.ubx.poo.ubgarden.game.go.bonus.*;
import fr.ubx.poo.ubgarden.game.go.decor.*;
import fr.ubx.poo.ubgarden.game.go.decor.ground.*;
import fr.ubx.poo.ubgarden.game.go.decor.nest.*;
import fr.ubx.poo.ubgarden.game.go.decor.special.*;
import fr.ubx.poo.ubgarden.game.launcher.MapEntity;
import fr.ubx.poo.ubgarden.game.launcher.MapLevel;
import javafx.application.Platform;

import java.util.Collection;
import java.util.HashMap;

public class Level implements Map {

    private final int level;
    private final int width;
    private final int height;
    private final HashMap<Position, Decor> decors = new HashMap<>();
    private final Game game;

    public Level(Game game, int level, MapLevel entities) {
        this.game = game;
        this.level = level;
        this.width = entities.width();
        this.height = entities.height();

        // Création de tous les décors
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Position position = new Position(level, i, j);
                MapEntity mapEntity = entities.get(i, j);
                Decor decor = createDecor(position, mapEntity);
                decors.put(position, decor);
            }
        }

        // Initialisation des nids après création de tous les décors
        initializeNests();
    }

    private Decor createDecor(Position position, MapEntity mapEntity) {
        switch (mapEntity) {
            case Grass:
                return new Grass(position);
            case Dirt:
                return new Dirt(position);
            case Tree:
                return new Tree(position);
            case Flowers:
                return new Flowers(position);
            case Carrot:
                Grass grassWithCarrot = new Grass(position);
                grassWithCarrot.setBonus(new Carrots(position, grassWithCarrot));
                return grassWithCarrot;
            case Hedgehog:
                return new Hedgehog(position);
            case ClosedDoor:
                return new ClosedDoor(position);
            case OpenedDoor:
                return new OpenedDoor(position);
            case WaspNest:
                return new WaspNest(position, game);
            case HornetNest:
                return new HornetNest(position, game);
            case Apple:
                Grass grassWithApple = new Grass(position);
                grassWithApple.setBonus(new EnergyBoost(position, grassWithApple));
                return grassWithApple;
            case PoisonedApple:
                Grass grassWithPoisonedApple = new Grass(position);
                grassWithPoisonedApple.setBonus(new PoisonedApple(position, grassWithPoisonedApple));
                return grassWithPoisonedApple;
            case InsectBomb:
                Grass grassWithBomb = new Grass(position);
                grassWithBomb.setBonus(new InsectBomb(position, grassWithBomb));
                return grassWithBomb;
            case Gardener:
                return new Grass(position);
            default:
                throw new RuntimeException("EntityCode " + mapEntity.name() + " not processed");
        }
    }

    private void initializeNests() {
        for (Decor decor : decors.values()) {
            if (decor instanceof Nest nest) {
                // Démarrer le spawn périodique
                nest.startSpawning();

                // Spawn initial immédiat
                Platform.runLater(() -> {
                    try {
                        nest.spawnEnemy();
                    } catch (Exception e) {
                        System.err.println("Error spawning enemy: " + e.getMessage());
                    }
                });
            }
        }
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public Decor get(Position position) {
        return decors.get(position);
    }

    @Override
    public Collection<Decor> values() {
        return decors.values();
    }

    @Override
    public boolean inside(Position position) {
        return position.x() >= 0 && position.x() < width && position.y() >= 0 && position.y() < height;
    }

    @Override
    public void put(Position position, Decor decor) {
        decors.put(position, decor);
    }
}