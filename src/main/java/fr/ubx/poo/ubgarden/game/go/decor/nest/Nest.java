package fr.ubx.poo.ubgarden.game.go.decor.nest;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.enemy.Enemy;
import fr.ubx.poo.ubgarden.game.go.enemy.Hornet;
import fr.ubx.poo.ubgarden.game.go.enemy.Wasp;
import javafx.application.Platform;

import java.util.Timer;
import java.util.TimerTask;

public abstract class Nest extends Decor {
    protected final Game game;
    private Timer spawnTimer;

    public Nest(Position position, Game game) {
        super(position);
        this.game = game;
        startSpawning();
    }

    public abstract int getSpawnRateMs();
    public abstract String getEnemyType();

    public void startSpawning() {
        spawnTimer = new Timer();
        spawnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> spawnEnemy());
            }
        }, getSpawnRateMs(), getSpawnRateMs());
    }

    public void spawnEnemy() {
        Position spawnPos = findSpawnPosition();
        if (spawnPos != null) {
            Enemy enemy;
            if (getEnemyType().equals("wasp")) {
                enemy = new Wasp(game, spawnPos, Direction.random());
            } else {
                enemy = new Hornet(game, spawnPos, Direction.random());
            }
            game.addEnemy(enemy);
            System.out.println(getEnemyType() + " spawned at " + spawnPos);
        }
    }

    private Position findSpawnPosition() {
        for (Direction direction : Direction.values()) {
            Position pos = direction.nextPosition(getPosition());
            if (game.world().getGrid().inside(pos)) {
                Decor decor = game.world().getGrid().get(pos);
                if (decor != null && decor.walkableBy(null)) {
                    return pos;
                }
            }
        }
        return null;
    }

    @Override
    public void remove() {
        super.remove();
        if (spawnTimer != null) {
            spawnTimer.cancel();
        }
    }
}