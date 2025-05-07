package fr.ubx.poo.ubgarden.game.go.decor;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

import fr.ubx.poo.ubgarden.game.Position;

public class Hedgehog extends Decor {
    public Hedgehog(Game game, Position position) {
        super(game,position);
    }

    @Override
    public boolean walkableBy(Gardener gardener) {
        return true;
    }

    @Override
    public void pickUpBy(Gardener gardener) {
        if (!isDeleted()) {
            System.out.println("Hedgehog found! Victory!");
            gardener.win(); // Déclenche la victoire
            this.remove(); // Supprime le hérisson
            gardener.getGame().end(true); // Termine le jeu avec victoire
        }
    }
}
