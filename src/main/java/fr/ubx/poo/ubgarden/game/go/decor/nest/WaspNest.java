package fr.ubx.poo.ubgarden.game.go.decor.nest;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;

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
}

