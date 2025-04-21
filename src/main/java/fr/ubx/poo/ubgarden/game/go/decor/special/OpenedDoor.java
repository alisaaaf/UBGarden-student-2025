package fr.ubx.poo.ubgarden.game.go.decor.special;

import fr.ubx.poo.ubgarden.game.Position;

public class OpenedDoor extends Door {

    public OpenedDoor(Position position) {
        super(position);
    }

    @Override
    public boolean isOpen() {
        return true;
    }
}
