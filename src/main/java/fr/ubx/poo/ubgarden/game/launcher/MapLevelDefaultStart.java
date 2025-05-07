package fr.ubx.poo.ubgarden.game.launcher;

import static fr.ubx.poo.ubgarden.game.launcher.MapEntity.*;

public class MapLevelDefaultStart extends MapLevel {
    private final static int width = 18;
    private final static int height = 8;
    private final MapEntity[][] level1 = {
            {Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass},
            {Grass, Gardener, Carrot, Apple, PoisonedApple, InsectBomb, Grass, Tree, Grass, Tree, Grass, Grass, Tree, Tree, Grass, Grass, Grass, Grass},
            {Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Carrot, Grass, Tree, Grass, Grass, Grass},
            {Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Tree, Grass, Grass, Grass, Grass},
            {Grass, Tree, Grass, Tree, Dirt, Grass, WaspNest, HornetNest, Grass, ClosedDoor, Grass, Grass, Grass, Tree, Grass, Flowers, Apple, Grass},
            {Grass, Tree, Tree, Tree, Grass, Grass, Grass, Grass, Carrot, Carrot, Carrot, Carrot, Carrot, Grass, Grass, Grass, Grass, Grass},
            {Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass},
            {Grass, Tree, Tree, Grass, Tree, Carrot, Carrot, Carrot, Carrot, Carrot, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass}
    };

    public MapLevelDefaultStart() {
        super(1, width, height);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                set(i, j, level1[j][i]);
    }
}