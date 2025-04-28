package fr.ubx.poo.ubgarden.game.go.decor.special;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.enemy.Enemy;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public class Flowers extends Decor {

    public Flowers(Position position) {
        super(position);
    }

    @Override
    public boolean walkableBy(Gardener gardener) {
        return false;
    }

    public boolean walkableByEnemy() {
        return true;
    }


    @Override
    public void pickUpBy(Gardener gardener) {
        // Aucun bonus à ramasser ici
    }
}
