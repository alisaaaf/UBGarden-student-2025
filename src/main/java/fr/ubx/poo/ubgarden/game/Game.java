package fr.ubx.poo.ubgarden.game;

import fr.ubx.poo.ubgarden.game.go.enemy.Enemy;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;
import fr.ubx.poo.ubgarden.game.go.decor.special.ClosedDoor;
import fr.ubx.poo.ubgarden.game.go.decor.special.OpenedDoor;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.bonus.Carrots;

import java.util.ArrayList;
import java.util.List;

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
        if (win)
            System.out.println("🏆 You win !");
        else
            System.out.println("☠️ You lost...");
        System.exit(0);
    }


    public void initCarrots() {
        this.carrotTotal = (int) world.getGrid().values().stream()
                .map(Decor::getBonus)
                .filter(bonus -> bonus instanceof Carrots)
                .count();
        System.out.println("🌱 Total carrots to collect: " + carrotTotal);
    }

    public void addCarrot() {
        carrotCount++;
        System.out.println("🥕 Carrot collected! Total: " + carrotCount + " / " + carrotTotal);

        if (carrotCount == carrotTotal) {
            System.out.println("✅ All carrots collected! Opening doors...");
            world.getGrid().values().stream()
                    .filter(decor -> decor instanceof ClosedDoor)
                    .map(decor -> (ClosedDoor) decor)
                    .forEach(closedDoor -> {
                        Position pos = closedDoor.getPosition();
                        OpenedDoor open = new OpenedDoor(pos);
                        world.getGrid().put(pos, open);
                        open.setModified(true);
                        System.out.println("🚪 Door opened at " + pos);
                    });
        }
    }



}
