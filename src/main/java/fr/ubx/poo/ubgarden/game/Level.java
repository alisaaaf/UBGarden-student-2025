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

                // Pour le niveau 2, vérifier le hérisson
                if (level == 2 && decor instanceof Hedgehog) {
                    System.out.println("Hedgehog placed at " + position);
                }
            }
        }

        initializeNests();
    }

    protected Decor createDecor(Position position, MapEntity mapEntity) {
        Grass ground = new Grass(game, position); // Par défaut, le sol est de l'herbe

        switch (mapEntity) {
            case Grass:
                return ground;
            case Dirt:
                return new Dirt(game, position);
            case Tree:
                return new Tree(game, position);
            case Flowers:
                return new Flowers(game, position);
            case Carrot: {
                Grass grassWithCarrot = new Grass(game, position);
                grassWithCarrot.setBonus(new Carrots(game, position, grassWithCarrot));
                return grassWithCarrot;
            }
            case Apple: {
                Grass grassWithApple = new Grass(game, position);
                grassWithApple.setBonus(new EnergyBoost(game, position, grassWithApple));
                return grassWithApple;
            }
            case PoisonedApple: {
                Grass grassWithPoisonedApple = new Grass(game, position);
                grassWithPoisonedApple.setBonus(new PoisonedApple(game, position, grassWithPoisonedApple));
                return grassWithPoisonedApple;
            }
            case InsectBomb: {
                Grass grassWithBomb = new Grass(game, position);
                grassWithBomb.setBonus(new InsectBomb(game, position, grassWithBomb));
                return grassWithBomb;
            }
            case ClosedDoor:
                return new ClosedDoor(game, position, 2);
            case OpenedDoor:
                return new OpenedDoor(game, position, 1);
            case WaspNest:
                return new WaspNest(position, game);
            case HornetNest:
                return new HornetNest(position, game);
            case Gardener:
                return ground;
            case Hedgehog:
                return new Hedgehog(game, position);
            default:
                throw new RuntimeException("EntityCode " + mapEntity.name() + " not processed");
        }
    }


    public void initializeNests() {
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
        // Clear existing enemies
        game.getEnemies().clear();

        // Add enemies from state
        for (Enemy enemy : state.getEnemies()) {
            game.addEnemy(enemy);
        }
    }


    public void applyState(LevelState state) {
        if (state == null) return;

        // Clear existing collected items
        for (Position pos : collectedItems) {
            Decor decor = decors.get(pos);
            if (decor != null && decor.getBonus() != null) {
                decor.setBonus(null);
                decor.setModified(true);
            }
        }
        collectedItems.clear();

        // Apply new state
        collectedItems.addAll(state.getCollectedItems());
        for (Position pos : collectedItems) {
            Decor decor = decors.get(pos);
            if (decor != null) {
                decor.setBonus(null);
                decor.setModified(true);
            }
        }

        // Update enemies
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