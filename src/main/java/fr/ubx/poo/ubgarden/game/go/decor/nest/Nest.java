package fr.ubx.poo.ubgarden.game.go.decor.nest;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.bonus.InsectBomb;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.decor.ground.Grass;
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
    protected void spawnBombs(int count) {
        for (int i = 0; i < count; i++) {
            Position bombPos = findRandomEmptyPosition();
            if (bombPos != null) {
                Grass grass = new Grass(bombPos);
                grass.setBonus(new InsectBomb(bombPos, grass));
                game.world().getGrid().put(bombPos, grass);
                grass.setModified(true);
            }
        }
    }

    private Position findRandomEmptyPosition() {
        int attempts = 0;
        while (attempts < 20) {
            int x = (int) (Math.random() * game.world().getGrid().width());
            int y = (int) (Math.random() * game.world().getGrid().height());
            Position pos = new Position(getPosition().level(), x, y);

            Decor decor = game.world().getGrid().get(pos);
            if (decor instanceof Grass && decor.getBonus() == null) {
                return pos;
            }
            attempts++;
        }
        return null;
    }

    public void spawnEnemy() {
        if (!game.world().getGrid().inside(getPosition())) {
            return; // Sécurité
        }

        Position spawnPos = findSpawnPosition();
        if (spawnPos != null) {
            Enemy enemy = createEnemy(spawnPos);
            game.addEnemy(enemy);
            System.out.println(getEnemyType() + " spawned at " + spawnPos);
            spawnBombs(getBombCount());
        } else {
            System.out.println("Could not find spawn position for nest at " + getPosition());
        }
    }

    protected abstract Enemy createEnemy(Position position);
    public abstract int getBombCount();


    Position findSpawnPosition() {
        for (Direction direction : Direction.values()) {
            Position pos = direction.nextPosition(getPosition());
            if (game.world().getGrid().inside(pos)) {
                Decor decor = game.world().getGrid().get(pos);
                if (decor != null && decor.walkableBy((Enemy) null)) {
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