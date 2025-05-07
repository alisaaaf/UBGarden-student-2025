package fr.ubx.poo.ubgarden.game.launcher;

import fr.ubx.poo.ubgarden.game.Position;

import static fr.ubx.poo.ubgarden.game.launcher.MapEntity.Gardener;
import static fr.ubx.poo.ubgarden.game.launcher.MapEntity.Grass;

public class MapLevel {

    private final int level; // 🆕 Niveau associé à la carte
    private final int width;
    private final int height;
    private final MapEntity[][] grid;

    private Position gardenerPosition = null;

    // 🆕 Nouveau constructeur avec niveau explicite
    public MapLevel(int level, int width, int height) {
        this.level = level;
        this.width = width;
        this.height = height;
        this.grid = new MapEntity[height][width];
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int getLevel() {
        return level;
    }

    public MapEntity get(int i, int j) {
        return grid[j][i];
    }

    public void set(int i, int j, MapEntity mapEntity) {
        grid[j][i] = mapEntity;
    }

    public Position getGardenerPosition() {
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                if (grid[j][i] == Gardener) {
                    if (gardenerPosition != null)
                        throw new RuntimeException("Multiple definition of gardener");
                    set(i, j, Grass);
                    // ✅ Utilise le vrai niveau de la carte
                    gardenerPosition = new Position(level, i, j);
                }
        return gardenerPosition;
    }
}
