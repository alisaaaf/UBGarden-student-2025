package fr.ubx.poo.ubgarden.game.go;


import fr.ubx.poo.ubgarden.game.go.enemy.Enemy;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public interface Walkable {

    /**
     * Checks whether the given {@link Gardener} can walk on this object.
     *
     * @param gardener the gardener attempting to walk
     * @return true if the gardener can walk on it, false otherwise
     */
    boolean walkableBy(Gardener gardener);
    default boolean walkableBy(Enemy enemy) {
        return true; // Par défaut, les ennemis peuvent passer
    }

    /**
     * Returns the amount of energy consumed when walking over this object.
     *
     * @return the energy cost of walking, defaults to 0
     */
    default int energyConsumptionWalk() {
        return 0;
    }
}
