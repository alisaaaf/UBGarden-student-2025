package fr.ubx.poo.ubgarden.game.go.enemy;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;

public class Wasp extends Enemy {

    public Wasp(Game game, Position position, Direction direction) {
        super(game, position, direction);
    }

    @Override
    public boolean canMove(Direction direction) {
        return true; // à adapter
    }

    @Override
    public Position move(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
        return nextPos;
    }

    @Override
    public int stingDamage() {
        return 20;
    }

    @Override
    public boolean isDeadAfterSting() {
        return true;
    }

    @Override
    public void onBombHit() {
        this.remove(); // сразу умирает
    }
}
