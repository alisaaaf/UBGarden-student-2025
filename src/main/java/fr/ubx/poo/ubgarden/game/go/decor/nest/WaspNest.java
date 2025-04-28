package fr.ubx.poo.ubgarden.game.go.decor.nest;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.enemy.Enemy;
import fr.ubx.poo.ubgarden.game.go.enemy.Wasp;

public class WaspNest extends Nest {
    public WaspNest(Position position, Game game) {
        super(position, game);
    }

    @Override
    public int getSpawnRateMs() {
        return 5000;
    }

    @Override
    public String getEnemyType() {
        return "wasp";
    }
    @Override
    protected Enemy createEnemy(Position position) {
        return new Wasp(game, position, Direction.random());
    }

    @Override
    public int getBombCount() {
        return 1;
    }

}

