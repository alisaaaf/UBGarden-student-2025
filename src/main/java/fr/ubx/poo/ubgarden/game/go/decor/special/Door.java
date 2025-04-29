package fr.ubx.poo.ubgarden.game.go.decor.special;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.enemy.Enemy;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public abstract class Door extends Decor {
    public Door(Position position) {
        super(position);
    }

    public abstract boolean isOpen();

    @Override
    public boolean walkableBy(Gardener gardener) {
        return isOpen();
    }

    @Override
    public boolean walkableBy(Enemy enemy) {
        return false;
    }
}
