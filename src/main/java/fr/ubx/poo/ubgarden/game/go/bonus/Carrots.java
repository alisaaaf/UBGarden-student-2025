package fr.ubx.poo.ubgarden.game.go.bonus;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public class Carrots extends Bonus {

    public Carrots(Game game, Position position, Decor decor) {
        super(game, position, decor);
    }

    @Override
    public void applyEffectTo(Gardener gardener) {
        gardener.getGame().addCarrot();
        System.out.println("🥕 Carrot picked!");
    }
}
