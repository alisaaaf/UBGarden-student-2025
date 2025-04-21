package fr.ubx.poo.ubgarden.game.go.decor.nest;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;

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
}
