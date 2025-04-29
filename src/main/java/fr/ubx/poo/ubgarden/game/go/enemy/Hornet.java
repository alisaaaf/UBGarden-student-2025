package fr.ubx.poo.ubgarden.game.go.enemy;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;

public class Hornet extends Enemy {
    private int hitPoints = 2;

    public Hornet(Game game, Position position, Direction direction) {
        super(game, position, direction,1000);
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
        return 30;
    }

    @Override
    public boolean isDeadAfterSting() {
        return --hitPoints <= 0;
    }

    @Override
    public void onBombHit() {
        hitPoints--;
        if (hitPoints <= 0) {
            this.remove();
        }
    }
}
