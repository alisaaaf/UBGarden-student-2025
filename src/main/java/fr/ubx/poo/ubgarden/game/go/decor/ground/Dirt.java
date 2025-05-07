package fr.ubx.poo.ubgarden.game.go.decor.ground;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.bonus.Bonus;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public class Dirt extends Ground {

    public Dirt(Game game , Position position) {
        super(game,position);
    }

    @Override
    public void pickUpBy(Gardener gardener) {
        Bonus bonus = getBonus();
        if (bonus != null) {
            bonus.pickUpBy(gardener);
        }
    }

    @Override
    public int energyConsumptionWalk() {
        return 2;
    }
}
