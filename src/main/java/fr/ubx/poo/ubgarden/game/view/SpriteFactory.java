package fr.ubx.poo.ubgarden.game.view;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.bonus.EnergyBoost;
import fr.ubx.poo.ubgarden.game.go.bonus.InsectBomb;
import fr.ubx.poo.ubgarden.game.go.bonus.PoisonedApple;
import fr.ubx.poo.ubgarden.game.go.decor.Carrots;
import fr.ubx.poo.ubgarden.game.go.decor.Tree;
import fr.ubx.poo.ubgarden.game.go.decor.ground.Dirt;
import fr.ubx.poo.ubgarden.game.go.decor.ground.Grass;
import fr.ubx.poo.ubgarden.game.go.decor.nest.Nest;
import fr.ubx.poo.ubgarden.game.go.decor.special.ClosedDoor;
import fr.ubx.poo.ubgarden.game.go.decor.special.Flowers;
import fr.ubx.poo.ubgarden.game.go.decor.special.OpenedDoor;
import fr.ubx.poo.ubgarden.game.go.enemy.Enemy;
import fr.ubx.poo.ubgarden.game.go.enemy.Wasp;
import fr.ubx.poo.ubgarden.game.go.enemy.Hornet;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;
import fr.ubx.poo.ubgarden.game.go.decor.Hedgehog;
import javafx.scene.layout.Pane;

import static fr.ubx.poo.ubgarden.game.view.ImageResource.*;

public final class SpriteFactory {

    public static Sprite create(Pane layer, GameObject gameObject) {
        ImageResourceFactory factory = ImageResourceFactory.getInstance();

        // Terrain décor
        if (gameObject instanceof Grass)
            return new Sprite(layer, factory.get(GRASS), gameObject);
        if (gameObject instanceof Dirt)
            return new Sprite(layer, factory.get(LAND), gameObject);
        if (gameObject instanceof Tree)
            return new Sprite(layer, factory.get(TREE), gameObject);
        if (gameObject instanceof Carrots)
            return new Sprite(layer, factory.get(CARROTS), gameObject);
        if (gameObject instanceof Flowers)
            return new Sprite(layer, factory.get(FLOWERS), gameObject);
        if (gameObject instanceof ClosedDoor)
            return new Sprite(layer, factory.get(DOOR_CLOSED), gameObject);
        if (gameObject instanceof OpenedDoor)
            return new Sprite(layer, factory.get(DOOR_OPENED), gameObject);

        // Static decor
        if (gameObject instanceof Hedgehog)
            return new Sprite(layer, factory.get(HEDGEHOG), gameObject);

        if (gameObject instanceof Nest nest) {
            return switch (nest.getEnemyType()) {
                case "wasp" -> new Sprite(layer, factory.get(NESTWASP), gameObject);
                case "hornet" -> new Sprite(layer, factory.get(NESTHORNET), gameObject);
                default -> throw new RuntimeException("Unknown nest type");
            };
        }

        // Bonuses
        if (gameObject instanceof EnergyBoost)
            return new Sprite(layer, factory.get(APPLE), gameObject);
        if (gameObject instanceof PoisonedApple)
            return new Sprite(layer, factory.get(POISONED_APPLE), gameObject);
        if (gameObject instanceof InsectBomb)
            return new Sprite(layer, factory.get(INSECTICIDE), gameObject);

        // Enemies (with direction)
        if (gameObject instanceof Enemy enemy) {
            Direction dir = enemy.getDirection();
            if (enemy instanceof Wasp) {
                return switch (dir) {
                    case UP -> new Sprite(layer, factory.get(WASP_UP), gameObject);
                    case RIGHT -> new Sprite(layer, factory.get(WASP_RIGHT), gameObject);
                    case DOWN -> new Sprite(layer, factory.get(WASP_DOWN), gameObject);
                    case LEFT -> new Sprite(layer, factory.get(WASP_LEFT), gameObject);
                };
            } else if (enemy instanceof Hornet) {
                return switch (dir) {
                    case UP -> new Sprite(layer, factory.get(HORNET_UP), gameObject);
                    case RIGHT -> new Sprite(layer, factory.get(HORNET_RIGHT), gameObject);
                    case DOWN -> new Sprite(layer, factory.get(HORNET_DOWN), gameObject);
                    case LEFT -> new Sprite(layer, factory.get(HORNET_LEFT), gameObject);
                };
            }
        }

        // Gardener
        if (gameObject instanceof Gardener gardener) {
            return new SpriteGardener(layer, gardener);
        }

        throw new RuntimeException("Unsupported sprite for object: " + gameObject.getClass());
    }
}
