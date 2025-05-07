package fr.ubx.poo.ubgarden.game;

import fr.ubx.poo.ubgarden.game.go.enemy.Enemy;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;
import fr.ubx.poo.ubgarden.game.go.decor.special.ClosedDoor;
import fr.ubx.poo.ubgarden.game.go.decor.special.OpenedDoor;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.bonus.Carrots;

import java.util.*;
import java.util.Map;

public class Game {

    private final Configuration configuration;
    private final World world;
    private final Gardener gardener;
    private boolean switchLevelRequested = false;
    private int switchLevel;
    private int carrotCount = 0;
    private int carrotTotal = 0;
    private final List<Enemy> enemies = new ArrayList<>();
    public List<Enemy> getEnemies() {
        return enemies;
    }
    private final Map<Integer, LevelState> levelStates = new HashMap<>();

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
        enemy.setModified(true);
    }


    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
    }
    public Game(World world, Configuration configuration, Position gardenerPosition) {
        this.configuration = configuration;
        this.world = world;
        gardener = new Gardener(this, gardenerPosition);
    }

    public Configuration configuration() {
        return configuration;
    }

    public Gardener getGardener() {
        return gardener;
    }

    public World world() {
        return world;
    }

    public boolean isSwitchLevelRequested() {
        return switchLevelRequested;
    }

    public int getSwitchLevel() {
        return switchLevel;
    }

    public void requestSwitchLevel(int level) {
        this.switchLevel = level;
        switchLevelRequested = true;
    }


    public void clearSwitchLevel() {
        switchLevelRequested = false;
    }

    public void end(boolean win) {
        if (win) {
            System.out.println("🏆 You win !");

        } else {
            System.out.println("☠️ You lost...");
        }
        System.exit(0);
    }


    public void initCarrots() {
        this.carrotTotal = (int) world.getGrid().values().stream()
                .filter(Objects::nonNull) // Ajout de ce filtre
                .map(Decor::getBonus)
                .filter(Objects::nonNull) // Et celui-ci
                .filter(bonus -> bonus instanceof Carrots)
                .count();
        System.out.println("🌱 Total carrots to collect: " + carrotTotal);
    }

    public void addCarrot() {
        carrotCount++;
        System.out.println("🥕 Carrot collected! Total: " + carrotCount + " / " + carrotTotal);

        if (carrotCount >= carrotTotal) {
            System.out.println("✅ All carrots collected! Opening doors...");
            openAllDoorsInCurrentLevel();
        }
    }

    private void openAllDoorsInCurrentLevel() {
        for (Decor decor : world.getGrid().values()) {
            if (decor instanceof ClosedDoor) {
                Position pos = decor.getPosition();
                int targetLevel = ((ClosedDoor) decor).getTargetLevel();
                OpenedDoor openDoor = new OpenedDoor(this, pos, targetLevel); // ✅ game ajouté
                world.getGrid().put(pos, openDoor);
                openDoor.setModified(true);
                System.out.println("🚪 Door opened at " + pos);
            }
        }
    }


    public void saveCurrentLevelState() {
        if (world.getGrid() instanceof Level) {
            Level currentLevel = (Level) world.getGrid();
            LevelState state = currentLevel.createCurrentState();
            levelStates.put(world.currentLevel(), state);
        }
    }

    public LevelState getLevelState(int level) {
        return levelStates.getOrDefault(level, new LevelState());
    }

    public void clearLevelState(int level) {
        levelStates.remove(level);
    }



}
