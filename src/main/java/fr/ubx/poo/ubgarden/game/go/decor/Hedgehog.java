package fr.ubx.poo.ubgarden.game.go.decor;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

import fr.ubx.poo.ubgarden.game.Position;

public class Hedgehog extends Decor {
    public Hedgehog(Position position) {
        super(position);
    }

    @Override
    public boolean walkableBy(Gardener gardener) {
        return true;
    }

    @Override
    public void pickUpBy(Gardener gardener) {
        gardener.win();
    }

}
