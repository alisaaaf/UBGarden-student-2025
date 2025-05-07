package fr.ubx.poo.ubgarden.game;

import fr.ubx.poo.ubgarden.game.go.bonus.*;
import fr.ubx.poo.ubgarden.game.go.decor.*;
import fr.ubx.poo.ubgarden.game.go.decor.ground.*;
import fr.ubx.poo.ubgarden.game.go.decor.nest.*;
import fr.ubx.poo.ubgarden.game.go.decor.special.*;
import fr.ubx.poo.ubgarden.game.go.enemy.Enemy;
import fr.ubx.poo.ubgarden.game.launcher.MapEntity;
import fr.ubx.poo.ubgarden.game.launcher.MapLevel;
import javafx.application.Platform;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Level implements Map {

    private final int level;
    private final int width;
    private final int height;
    private final HashMap<Position, Decor> decors = new HashMap<>();
    private final Game game;
    private final Set<Position> collectedItems = new HashSet<>();

    public Level(Game game, int level, MapLevel entities) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
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
        if (level == 2) {
            System.out.println("Level 2 initialized with " + decors.size() + " decors");
        }

        // Initialisation des nids après création de tous les décors
        initializeNests(true);
    }

    protected Decor createDecor(Position position, MapEntity mapEntity) {
        Grass base = new Grass(position);
        switch (mapEntity) {
            case Grass:
                return base;
            case Dirt:
                return new Dirt(position);
            case Tree:
                return new Tree(position);
            case Flowers:
                return new Flowers(position);
            case Carrot:
                base.setBonus(new Carrots(position, base));
                return base;
            case Hedgehog:
                return new Hedgehog(position);
            case ClosedDoor:
                return new ClosedDoor(position, 2);
            case OpenedDoor:
                return new OpenedDoor(position, 1);
            case WaspNest:
                return new WaspNest(position, game);
            case HornetNest:
                return new HornetNest(position, game);
            case Apple:
                base.setBonus(new EnergyBoost(position, base));
                return base;
            case PoisonedApple:
                base.setBonus(new PoisonedApple(position, base));
                return base;
            case InsectBomb:
                base.setBonus(new InsectBomb(position, base));
                return base;
            case Gardener:
                return base; // le jardinier sera créé ailleurs
            default:
                throw new RuntimeException("EntityCode " + mapEntity.name() + " not processed");
        }
    }

    public void initializeNests(boolean spawnInitial) {
        for (Decor decor : decors.values()) {
            if (decor instanceof Nest nest) {
                nest.startSpawning();
                if (spawnInitial) {
                    Platform.runLater(() -> {
                        try {
                            nest.spawnEnemy();
                            System.out.println("Enemy spawned from nest at " + nest.getPosition());
                        } catch (Exception e) {
                            System.err.println("Error spawning enemy: " + e.getMessage());
                        }
                    });
                }
            }
        }
    }

    public void applyLevelState(LevelState state) {
        if (state == null) return;

        // Supprimer les bonus déjà collectés
        for (Position pos : state.getCollectedItems()) {
            Decor decor = decors.get(pos);
            if (decor != null && decor.getBonus() != null) {
                decor.setBonus(null);
                decor.setModified(true);
            }
        }
        this.collectedItems.addAll(state.getCollectedItems());

        // Mettre à jour les ennemis
        updateEnemiesFromState(state);
    }

    private void updateEnemiesFromState(LevelState state) {
        game.getEnemies().clear();

        for (Enemy enemy : state.getEnemies()) {
            game.addEnemy(enemy);
        }
    }


    public void applyState(LevelState state) {
        if (state == null) return;

        for (Position pos : collectedItems) {
            Decor decor = decors.get(pos);
            if (decor != null && decor.getBonus() != null) {
                decor.setBonus(null);
                decor.setModified(true);
            }
        }
        collectedItems.clear();


        collectedItems.addAll(state.getCollectedItems());
        for (Position pos : collectedItems) {
            Decor decor = decors.get(pos);
            if (decor != null) {
                decor.setBonus(null);
                decor.setModified(true);
            }
        }

        game.getEnemies().clear();
        for (Enemy enemy : state.getEnemies()) {
            game.addEnemy(enemy);
        }
    }
    public LevelState createCurrentState() {
        LevelState state = new LevelState();
        state.getCollectedItems().addAll(collectedItems);
        state.getEnemies().addAll(game.getEnemies());
        return state;
    }
    public void markItemAsCollected(Position position) {
        collectedItems.add(position);
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