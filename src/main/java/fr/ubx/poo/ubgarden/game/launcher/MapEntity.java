package fr.ubx.poo.ubgarden.game.launcher;

public enum MapEntity {
    // Bonus
    Apple('+'),
    PoisonedApple('-'),
    Carrot('C'),
    InsectBomb('B'),

    // Decor
    Grass('G'),
    Dirt('D'),
    Tree('T'),
    Flowers('F'),

    // Doors
    ClosedDoor('X'),
    OpenedDoor('O'),

    // Nests
    WaspNest('W'),
    HornetNest('Z'),

    // Personnages
    Gardener('P'),
    Hedgehog('H');

    private final char code;

    MapEntity(char c) {
        this.code = c;
    }

    public static MapEntity fromCode(char c) {
        for (MapEntity mapEntity : values()) {
            if (mapEntity.code == c)
                return mapEntity;
        }
        throw new MapException("Invalid character " + c);
    }

    public char getCode() {
        return this.code;
    }

    @Override
    public String toString() {
        return Character.toString(code);
    }
}
