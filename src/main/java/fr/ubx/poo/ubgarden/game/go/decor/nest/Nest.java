package fr.ubx.poo.ubgarden.game.go.decor.nest;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;

public abstract class Nest extends Decor {
    protected final Game game;

    public Nest(Position position, Game game) {
        super(position);
        this.game = game;
    }

    public abstract int getSpawnRateMs();

    public abstract String getEnemyType();
}
