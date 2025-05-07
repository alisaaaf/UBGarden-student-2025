package fr.ubx.poo.ubgarden.game.launcher;

import java.io.*;
import java.util.*;

public class MapLevelFromFile extends MapLevel {

    public MapLevelFromFile(File file, int level) throws IOException {
        super(level, 0, 0); // 🆗 Dimensions temporaires, mais niveau réel passé ici

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

        int height = lines.size();
        int width = lines.get(0).replace(" ", "").length();

        // 🛠️ Injection dynamique des vraies dimensions et grille
        MapLevelSetter.set(this, width, height, new MapEntity[height][width]);

        // Remplir la grille avec les entités
        for (int y = 0; y < height; y++) {
            String line = lines.get(y).replace(" ", "");
            for (int x = 0; x < width; x++) {
                char c = line.charAt(x);
                set(x, y, MapEntity.fromCode(c));
            }
        }
    }
}
