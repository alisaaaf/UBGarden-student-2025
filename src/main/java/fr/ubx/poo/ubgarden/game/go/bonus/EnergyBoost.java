package fr.ubx.poo.ubgarden.game.go.bonus;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public class EnergyBoost extends Bonus {

    public EnergyBoost(Position position, Decor decor) {
        super(position, decor);
    }

    @Override
    public void applyEffectTo(Gardener gardener) {
        int boost = gardener.getGame().configuration().energyBoost();
        int max = gardener.getGame().configuration().gardenerEnergy();
        gardener.setEnergy(Math.min(gardener.getEnergy() + boost, max));
        gardener.resetDisease(); // лечим болезнь
        System.out.println("🍎 Apple consumed: + " + boost + " energy (max = " + max + ")");
    }
}
