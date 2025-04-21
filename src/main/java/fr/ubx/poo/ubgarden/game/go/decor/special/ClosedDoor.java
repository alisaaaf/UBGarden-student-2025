package fr.ubx.poo.ubgarden.game.go.decor.special;

import fr.ubx.poo.ubgarden.game.Position;

public class ClosedDoor extends Door {
    public ClosedDoor(Position position) {
        super(position);
    }

    @Override
    public boolean isOpen() {
        return false;
    }
}
