/*
 * Copyright (c) 2020. Laurent Réveillère
 */

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




public class Gardener extends GameObject implements Movable, PickupVisitor, WalkVisitor {

    private int energy;
    private Direction direction;
    private boolean moveRequested = false;
    private int diseaseLevel = 1;
    private long diseaseEndTime = 0;
    private int bombCount = 0;


    public Gardener(Game game, Position position) {

        super(game, position);
        this.direction = Direction.DOWN;
        this.energy = game.configuration().gardenerEnergy();
    }

    @Override
    public void pickUp(EnergyBoost energyBoost) {

        System.out.println("I am taking the boost, I should do something ...");

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
        if (decor != null && !decor.walkableBy(this)) {
            return false;
        }

        return true;
    }

    @Override
    public Position move(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor next = game.world().getGrid().get(nextPos);
        setPosition(nextPos);
        if (next != null)
            next.pickUpBy(this);
        return nextPos;
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                Position nextPos = direction.nextPosition(getPosition());
                Decor nextDecor = game.world().getGrid().get(nextPos);

                int cost = 1;
                if (nextDecor instanceof fr.ubx.poo.ubgarden.game.go.decor.ground.Dirt)
                    cost = 2;

                int totalCost = cost * diseaseLevel;
                energy -= totalCost;

                System.out.println("Moved to " + nextPos + ", energy cost: " + totalCost + ", remaining: " + energy);

                move(direction);

                if (energy <= 0) {
                    die();
                }
            }
        }

        moveRequested = false;
    }


    public Direction getDirection() {
        return direction;
    }

    public void recoverEnergy() {
        int maxEnergy = game.configuration().gardenerEnergy();
        int boost = game.configuration().energyBoost();
        this.energy = Math.min(this.energy + boost, maxEnergy);
        System.out.println("Energy recovered: " + energy);
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



}
