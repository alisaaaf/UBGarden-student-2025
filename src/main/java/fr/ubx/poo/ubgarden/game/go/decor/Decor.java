package fr.ubx.poo.ubgarden.game.go.decor;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.Pickupable;
import fr.ubx.poo.ubgarden.game.go.Walkable;
import fr.ubx.poo.ubgarden.game.go.bonus.Bonus;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public abstract class Decor extends GameObject implements Walkable, Pickupable {

    private Bonus bonus;

    public Decor(Position position) {
        super(position);

    }

    public Bonus getBonus() {
        return bonus;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    @Override
    public boolean walkableBy(Gardener gardener) {
        // Si gardener est null, on considère que c'est walkable pour les ennemis
        return gardener == null || gardener.canWalkOn(this);
    }


    @Override
    public void update(long now) {
        super.update(now);
        if (bonus != null) bonus.update(now);
    }

}