package fr.ubx.poo.ubgarden.game.go.personage;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.Movable;
import fr.ubx.poo.ubgarden.game.go.PickupVisitor;
import fr.ubx.poo.ubgarden.game.go.WalkVisitor;
import fr.ubx.poo.ubgarden.game.go.bonus.EnergyBoost;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.bonus.PoisonedApple;
import fr.ubx.poo.ubgarden.game.go.bonus.InsectBomb;
import fr.ubx.poo.ubgarden.game.go.bonus.Bonus;
import fr.ubx.poo.ubgarden.game.go.enemy.Enemy;
import fr.ubx.poo.ubgarden.game.go.enemy.Hornet;
import fr.ubx.poo.ubgarden.game.go.enemy.Wasp;


public class Gardener extends GameObject implements Movable, PickupVisitor, WalkVisitor {

    private int energy;
    private Direction direction;
    private boolean moveRequested = false;
    private int diseaseLevel = 1;
    private long diseaseEndTime = 0;
    private int bombCount = 0;
    private long lastMoveTime = 0;

    public Gardener(Game game, Position position) {
        super(game, position);
        this.direction = Direction.DOWN;
        this.energy = game.configuration().gardenerEnergy();
        this.lastMoveTime = System.currentTimeMillis();
    }

    public void pickUp(Bonus bonus) {
        bonus.applyEffectTo(this);
    }


    public int getEnergy() {
        return this.energy;
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
            setModified(true);
        }
        moveRequested = true;
    }

    @Override
    public final boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());

        if (!game.world().getGrid().inside(nextPos))
            return false;

        Decor decor = game.world().getGrid().get(nextPos);
        return decor == null || decor.walkableBy(this);
    }

    @Override
    public Position move(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Position realNextPos = new Position(game.world().currentLevel(), nextPos.x(), nextPos.y());
        Decor next = game.world().getGrid().get(realNextPos);
        setPosition(realNextPos);
        if (next != null)
            next.pickUpBy(this);
        return realNextPos;
    }


    @Override
    public void update(long now) {
        long currentTime = System.currentTimeMillis();

        if (diseaseLevel > 1 && currentTime > diseaseEndTime) {
            diseaseLevel--;
            diseaseEndTime = currentTime + game.configuration().diseaseDuration();
            System.out.println("💀 Disease effect ended, new diseaseLevel = " + diseaseLevel);
        }

        if (moveRequested) {
            if (canMove(direction)) {
                Position nextPos = direction.nextPosition(getPosition());
                Decor nextDecor = game.world().getGrid().get(nextPos);

                int cost = (nextDecor instanceof fr.ubx.poo.ubgarden.game.go.decor.ground.Dirt) ? 2 : 1;
                int totalCost = cost * diseaseLevel;
                energy -= totalCost;

                System.out.println("➡️ Moved to " + nextPos + " | Energy cost: " + totalCost + " | Remaining: " + energy);

                move(direction);
                lastMoveTime = now;

                if (energy <= 0) {
                    die();
                }
            }
            moveRequested = false;
        } else {

            if (now - lastMoveTime >= game.configuration().energyRecoverDuration() * 1_000_000L) {
                recoverEnergy();
                lastMoveTime = now;
            }
        }
    }


    public void recoverEnergy() {
        int maxEnergy = game.configuration().gardenerEnergy();
        int boost = game.configuration().energyBoost();
        this.energy = Math.min(this.energy + boost, maxEnergy);
    }


    public Direction getDirection() {
        return direction;
    }

    public void increaseDisease() {
        diseaseLevel++;
        diseaseEndTime = System.currentTimeMillis() + game.configuration().diseaseDuration();
        System.out.println("Disease level now: " + diseaseLevel);
    }

    public void addBomb() {
        bombCount++;
        System.out.println("Collected bomb! Now have: " + bombCount);
    }
    public void useBombIfNeeded() {
        if (bombCount <= 0) return;

        // Vérifier si un ennemi est adjacent
        for (Enemy enemy : game.getEnemies()) {
            if (isAdjacent(getPosition(), enemy.getPosition())) {
                if (enemy instanceof Wasp) {
                    enemy.onBombHit();
                    bombCount--;
                    System.out.println("💣 Used bomb on wasp! Bombs left: " + bombCount);
                    return;
                } else if (enemy instanceof Hornet) {
                    if (bombCount >= 2) {
                        enemy.onBombHit();
                        enemy.onBombHit(); // Deux coups pour un frelon
                        bombCount -= 2;
                        System.out.println("💣💣 Used two bombs on hornet! Bombs left: " + bombCount);
                        return;
                    }
                }
            }
        }
    }

    private boolean isAdjacent(Position pos1, Position pos2) {
        int dx = Math.abs(pos1.x() - pos2.x());
        int dy = Math.abs(pos1.y() - pos2.y());
        return (dx <= 1 && dy <= 1) && (dx + dy > 0);
    }

    public void win() {
        game.end(true);
    }

    public void hurt(int damage) {
        this.energy -= damage;
        if (this.energy <= 0) {
            die();
        }
    }

    public void die() {
        System.out.println("💀 Defeat! The gardener has no more energy.");
        game.end(false);
    }

    public int getBombCount() {
        return bombCount;
    }

    public int getDiseaseLevel() {
        return diseaseLevel;
    }

    public void resetDisease() {
        diseaseLevel = 1;
        diseaseEndTime = 0;
    }


    public void setEnergy(int energy) {
        this.energy = energy;
    }

}
