package fr.ubx.poo.ubgarden.game.go.decor.special;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public abstract class Door extends Decor {
    private final int targetLevel;

    public Door(Game game, Position position, int targetLevel) {
        super(game,position);
        this.targetLevel = targetLevel;
    }

    public int getTargetLevel() {
        return targetLevel;
    }

    public abstract boolean isOpen();

    @Override
    public boolean walkableBy(Gardener gardener) {
        return isOpen();
    }

    @Override
    public void pickUpBy(Gardener gardener) {
        if (isOpen()) {
            System.out.println("Entering door to level " + targetLevel);
            gardener.getGame().requestSwitchLevel(targetLevel);
        } else {
            System.out.println("Door is locked! Collect all carrots first.");
        }
    }
}