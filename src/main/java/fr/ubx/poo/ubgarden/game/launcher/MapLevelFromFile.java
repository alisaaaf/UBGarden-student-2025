package fr.ubx.poo.ubgarden.game.launcher;

import java.io.*;
import java.util.*;

public class MapLevelFromFile extends MapLevel {
    public MapLevelFromFile(File file) throws IOException {
        super(0, 0); // Dimensions temporaires

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
        }

        // Détermine les dimensions
        int height = lines.size();
        int width = lines.get(0).replace(" ", "").length();
        MapLevelSetter.set(this, width, height, new MapEntity[height][width]);

        // Remplissage de la grille
        for (int y = 0; y < height; y++) {
            String line = lines.get(y).replace(" ", "");
            for (int x = 0; x < width; x++) {
                char c = line.charAt(x);
                set(x, y, MapEntity.fromCode(c));
            }
        }
    }
}
