package fr.ubx.poo.ubgarden.game.go.enemy;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.engine.Timer;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.Movable;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.decor.special.Door;
import fr.ubx.poo.ubgarden.game.go.decor.special.Flowers;

public abstract class Enemy extends GameObject implements Movable {

    private Direction direction;
    private Timer moveTimer;
    private int moveDelay;

    public Enemy(Game game, Position position, Direction direction,int moveDelay) {
        super(game, position);
        this.direction = direction;
        this.moveDelay = moveDelay;
        this.moveTimer = new Timer(moveDelay);
        setModified(true);
    }

    public Direction getDirection() {
        return direction;
    }
    @Override
    public void update(long now) {
        moveTimer.update(now);
        if (!moveTimer.isRunning()) {
            moveRandomly();
            moveTimer.start();
        }
    }

    protected void moveRandomly() {
        Direction newDirection = Direction.random();
        setDirection(newDirection); // Mettre à jour la direction visuelle

        if (canMove(newDirection)) {
            move(newDirection);
        } else {
            // Choisir une autre direction si bloqué
            for (Direction dir : Direction.values()) {
                if (canMove(dir)) {
                    move(dir);
                    break;
                }
            }
        }
    }
    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());

        if (!getGame().world().getGrid().inside(nextPos)) {
            return false;
        }

        Decor decor = getGame().world().getGrid().get(nextPos);

        if (decor == null) {
            return true;
        }

        // Utilisez la nouvelle méthode walkableBy(Enemy)
        return decor.walkableBy(this);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public abstract int stingDamage();
    public abstract boolean isDeadAfterSting();

    public abstract void onBombHit();
}

