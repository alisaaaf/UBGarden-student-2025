package fr.ubx.poo.ubgarden.game;

import fr.ubx.poo.ubgarden.game.go.enemy.Enemy;
import fr.ubx.poo.ubgarden.game.Position;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

public class LevelState {
    private final Set<Position> collectedItems = new HashSet<>();
    private final List<Enemy> enemies = new ArrayList<>();

    public void addCollectedItem(Position position) {
        collectedItems.add(position);
    }

    public Set<Position> getCollectedItems() {
        return collectedItems;
    }

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    public List<Enemy> getEnemies() {
        return new ArrayList<>(enemies);
    }
}