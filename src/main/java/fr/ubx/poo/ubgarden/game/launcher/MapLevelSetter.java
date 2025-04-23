package fr.ubx.poo.ubgarden.game.launcher;

import java.lang.reflect.Field;

public class MapLevelSetter {
    public static void set(MapLevel map, int width, int height, MapEntity[][] grid) {
        try {
            Field w = MapLevel.class.getDeclaredField("width");
            Field h = MapLevel.class.getDeclaredField("height");
            Field g = MapLevel.class.getDeclaredField("grid");
            w.setAccessible(true);
            h.setAccessible(true);
            g.setAccessible(true);
            w.setInt(map, width);
            h.setInt(map, height);
            g.set(map, grid);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject values into MapLevel", e);
        }
    }
}
