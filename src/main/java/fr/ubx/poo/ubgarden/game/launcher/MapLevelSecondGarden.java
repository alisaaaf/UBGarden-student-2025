package fr.ubx.poo.ubgarden.game.launcher;

import static fr.ubx.poo.ubgarden.game.launcher.MapEntity.*;

public class MapLevelSecondGarden extends MapLevel {
    private final static int width = 10;  // Corrigé pour correspondre au tableau
    private final static int height = 10; // Corrigé pour correspondre au tableau
    private final MapEntity[][] level2 = {
            {Grass, Grass, Grass, Tree, Grass, Grass, Grass, Grass, Tree, Grass},
            {Grass, OpenedDoor, Grass, Tree, Grass, Hedgehog, Grass, Grass, Tree, Grass}, // Un seul hérisson ici
            {Grass, Grass, Grass, Tree, Grass, Grass, Grass, Grass, Tree, Grass},
            {Tree, Tree, Tree, Tree, Tree, Tree, Grass, Tree, Tree, Tree},
            {Grass, Grass, Grass, Grass, Carrot, Carrot, Grass, Grass, Grass, Grass}, // Ajout de carottes
            {Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass},
            {Grass, Grass, Tree, Grass, Apple, Grass, Grass, Grass, Grass, Grass}, // Ajout de pommes
            {Grass, Grass, Tree, Grass, Grass, Grass, Tree, Grass, Grass, Grass},
            {Grass, Grass, Tree, Grass, PoisonedApple, Grass, Tree, Grass, Grass, Grass},
            {Grass, Grass, Tree, Grass, Grass, Grass, Tree, Grass, Grass, Grass}
    };
    public MapLevelSecondGarden() {
        super(width, height);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                set(i, j, level2[j][i]);
    }
}