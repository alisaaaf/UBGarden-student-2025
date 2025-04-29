package fr.ubx.poo.ubgarden.game.go.enemy;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;

public class Wasp extends Enemy {

    public Wasp(Game game, Position position, Direction direction) {
        super(game, position, direction,500);
    }


    @Override
    public Position move(Direction direction) {
        if (canMove(direction)) {
            Position nextPos = direction.nextPosition(getPosition());
            setPosition(nextPos);
            return nextPos;
        }
        return getPosition();
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
