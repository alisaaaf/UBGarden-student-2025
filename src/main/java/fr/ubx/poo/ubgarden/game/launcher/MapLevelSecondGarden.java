package fr.ubx.poo.ubgarden.game.launcher;

import static fr.ubx.poo.ubgarden.game.launcher.MapEntity.*;

public class MapLevelSecondGarden extends MapLevel {
    private final static int width = 10;
    private final static int height = 10;
    private final MapEntity[][] level2 = {
            {Tree, Tree, Tree, Tree, Tree, Tree, Tree, Tree, Tree, Tree},
            {Tree, OpenedDoor, Grass, Grass, Grass, WaspNest, Grass, Grass, Grass, Tree},
            {Tree, Grass, Grass, Dirt, Dirt, Grass, Grass, HornetNest, Grass, Tree},
            {Tree, Grass, Dirt, Carrot, Carrot, Dirt, Grass, Grass, Grass, Tree},
            {Tree, Grass, Dirt, Carrot, Hedgehog, Dirt, Grass, Grass, Grass, Tree},
            {Tree, Grass, Grass, Dirt, Dirt, Grass, Grass, Grass, Grass, Tree},
            {Tree, Grass, Grass, Grass, Apple, Grass, Grass, PoisonedApple, Grass, Tree},
            {Tree, Grass, InsectBomb, Grass, Grass, Grass, Grass, Grass, Grass, Tree},
            {Tree, Grass, Grass, Grass, Flowers, Grass, Flowers, Grass, Grass, Tree},
            {Tree, Tree, Tree, Tree, Tree, Tree, Tree, Tree, Tree, Tree}
    };

    public MapLevelSecondGarden() {
        super(2, width, height);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                set(i, j, level2[j][i]);
    }
}