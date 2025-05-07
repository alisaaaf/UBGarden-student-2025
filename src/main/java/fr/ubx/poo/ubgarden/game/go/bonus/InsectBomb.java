package fr.ubx.poo.ubgarden.game.go.bonus;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public class InsectBomb extends Bonus {

    public InsectBomb(Position position, Decor decor) {
        super(position, decor);
    }

    @Override
    public void applyEffectTo(Gardener gardener) {
        gardener.addBomb();
    }

}
