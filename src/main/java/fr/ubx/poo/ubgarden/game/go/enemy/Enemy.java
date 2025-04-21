package fr.ubx.poo.ubgarden.game.go.enemy;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.Movable;

public abstract class Enemy extends GameObject implements Movable {

    private Direction direction;

    public Enemy(Game game, Position position, Direction direction) {
        super(game, position);
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public abstract int stingDamage();
    public abstract boolean isDeadAfterSting();

    public abstract void onBombHit();
}

