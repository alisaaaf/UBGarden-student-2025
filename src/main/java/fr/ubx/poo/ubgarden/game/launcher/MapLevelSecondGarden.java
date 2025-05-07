package fr.ubx.poo.ubgarden.game.launcher;

import static fr.ubx.poo.ubgarden.game.launcher.MapEntity.*;

public class MapLevelSecondGarden extends MapLevel {
    private final static int width = 18;
    private final static int height = 8;
    private final MapEntity[][] level2 = {

            {Tree,  Tree,    Tree,    Tree,    Tree,    Tree,    Tree,    Tree,    Tree,    Tree,    Tree,    Tree,    Tree,    Tree,    Tree,    Tree,    Tree,    Tree}, // Ligne 0
            {Tree,  OpenedDoor,Grass,  Grass,   WaspNest,Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Tree}, // Ligne 1
            {Tree,  Grass,   Dirt,    Dirt,    Grass,   Grass,   HornetNest,Grass,  Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Tree}, // Ligne 2
            {Tree,  Grass,   Grass,   Grass,   Grass,   Dirt,    Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Flowers, Grass,   Tree}, // Ligne 3
            {Tree,  Grass,   Carrot,  Carrot,  Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Hedgehog, Grass,   Grass,   Tree}, // Ligne 4
            {Tree,  Grass,   Grass,   Dirt,    Grass,   Dirt,    Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Tree}, // Ligne 5
            {Tree,  Grass,   Apple,   Grass,   PoisonedApple,Grass,Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Grass,   Tree}, // Ligne 6
            {Tree,  Tree,    Tree,    Tree,    Tree,    Tree,    Tree,    Tree,    Tree,    Tree,    Tree,    Tree,    Tree,    Tree,    Tree,    Tree,    Tree,    Tree}  // Ligne 7
    };

    public MapLevelSecondGarden() {
        super(width, height);


        set(1, 1, OpenedDoor);  // Porte de retour
        set(4, 1, WaspNest);    // Nid de guêpes
        set(6, 2, HornetNest);  // Nid de frelons
        set(2, 4, Carrot);      // Carottes
        set(3, 4, Carrot);
        set(14, 4, Hedgehog);   // Hérisson accessible
        set(2, 6, Apple);       // Pomme
        set(4, 6, PoisonedApple); // Pomme empoisonnée
        set(15, 3, Flowers);    // Fleurs décoratives


        set(5, 1, InsectBomb);
        set(10, 2, InsectBomb);


        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (level2[j][i] != null) {
                    set(i, j, level2[j][i]);
                } else {
                    set(i, j, Grass);
                }
            }
        }
    }
}