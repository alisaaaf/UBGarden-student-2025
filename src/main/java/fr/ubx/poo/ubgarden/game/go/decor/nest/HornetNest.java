package fr.ubx.poo.ubgarden.game.go.decor.nest;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.enemy.Enemy;
import fr.ubx.poo.ubgarden.game.go.enemy.Hornet;

public class HornetNest extends Nest {
    public HornetNest(Position position, Game game) {
        super(position, game);
    }

    @Override
    public int getSpawnRateMs() {
        return 10000;
    }

    @Override
    public String getEnemyType() {
        return "hornet";
    }
    @Override
    protected Enemy createEnemy(Position position) {
        return new Hornet(game, position, Direction.random());
    }

    @Override
    public int getBombCount() {
        return 2;
    }

}
