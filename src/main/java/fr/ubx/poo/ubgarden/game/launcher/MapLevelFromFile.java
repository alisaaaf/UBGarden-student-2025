package fr.ubx.poo.ubgarden.game.launcher;

import java.io.*;
import java.util.*;

public class MapLevelFromFile extends MapLevel {

    public MapLevelFromFile(File file) throws IOException {
        super(0, 0);

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        }

        int height = lines.size();
        int width = lines.get(0).length();
        MapEntity[][] grid = new MapEntity[height][width];

        for (int j = 0; j < height; j++) {
            String line = lines.get(j);
            for (int i = 0; i < width; i++) {
                grid[j][i] = MapEntity.fromCode(line.charAt(i));
            }
        }

        MapLevelSetter.set(this, width, height, grid);
    }
}
