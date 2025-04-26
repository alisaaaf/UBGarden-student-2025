package fr.ubx.poo.ubgarden.game;

import fr.ubx.poo.ubgarden.game.go.bonus.*;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.decor.Tree;
import fr.ubx.poo.ubgarden.game.go.decor.Hedgehog;
import fr.ubx.poo.ubgarden.game.go.decor.ground.Dirt;
import fr.ubx.poo.ubgarden.game.go.decor.ground.Grass;
import fr.ubx.poo.ubgarden.game.go.decor.special.ClosedDoor;
import fr.ubx.poo.ubgarden.game.go.decor.special.Flowers;
import fr.ubx.poo.ubgarden.game.go.decor.special.OpenedDoor;
import fr.ubx.poo.ubgarden.game.go.decor.nest.WaspNest;
import fr.ubx.poo.ubgarden.game.go.decor.nest.HornetNest;
import fr.ubx.poo.ubgarden.game.launcher.MapEntity;
import fr.ubx.poo.ubgarden.game.launcher.MapLevel;

import java.util.Collection;
import java.util.HashMap;

public class Level implements Map {

    private final int level;
    private final int width;
    private final int height;
    private final java.util.Map<Position, Decor> decors = new HashMap<>();

    public Level(Game game, int level, MapLevel entities) {
        this.level = level;
        this.width = entities.width();
        this.height = entities.height();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Position position = new Position(level, i, j);
                MapEntity mapEntity = entities.get(i, j);
                Decor decor = null;

                switch (mapEntity) {
                    case Grass -> decor = new Grass(position);
                    case Dirt -> decor = new Dirt(position);
                    case Tree -> decor = new Tree(position);
                    case Flowers -> decor = new Flowers(position);
                    case Carrot -> {
                        decor = new Grass(position);
                        decor.setBonus(new Carrots(position, decor));
                    }
                    case Hedgehog -> decor = new Hedgehog(position);
                    case ClosedDoor -> decor = new ClosedDoor(position);
                    case OpenedDoor -> decor = new OpenedDoor(position);
                    case WaspNest -> decor = new WaspNest(position, game);
                    case HornetNest -> decor = new HornetNest(position, game);
                    case Apple -> {
                        decor = new Grass(position);
                        decor.setBonus(new EnergyBoost(position, decor));
                    }
                    case PoisonedApple -> {
                        decor = new Grass(position);
                        decor.setBonus(new PoisonedApple(position, decor));
                    }
                    case InsectBomb -> {
                        decor = new Grass(position);
                        decor.setBonus(new InsectBomb(position, decor));
                    }
                    case Gardener -> decor = new Grass(position);
                    default -> throw new RuntimeException("EntityCode " + mapEntity.name() + " not processed");
                }

                decors.put(position, decor);
            }
        }
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public Decor get(Position position) {
        return decors.get(position);
    }

    @Override
    public Collection<Decor> values() {
        return decors.values();
    }

    @Override
    public boolean inside(Position position) {
        return position.x() >= 0 && position.x() < width && position.y() >= 0 && position.y() < height;
    }

    @Override
    public void put(Position position, Decor decor) {
        decors.put(position, decor);
    }
}
