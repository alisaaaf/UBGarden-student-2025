package fr.ubx.poo.ubgarden.game.go.decor.special;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;

public class ClosedDoor extends Door {
    public ClosedDoor(Game game, Position position, int targetLevel) {
        super(game,position, targetLevel);
    }

    @Override
    public boolean isOpen() {
        return false;
    }
}