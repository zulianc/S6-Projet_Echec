package structure;

public enum Orientation {
    FRONT(new Position(0, -1)),
    BACK(new Position(0, 1)),
    LEFT(new Position(-1, 0)),
    RIGHT(new Position(1, 0)),
    FRONT_LEFT(new Position(-1, -1)),
    FRONT_RIGHT(new Position(1, -1)),
    BACK_LEFT(new Position(-1, 1)),
    BACK_RIGHT(new Position(1, 1));

    private final Position vector;

    Orientation(Position vector) {
        this.vector = vector;
    }

    public Position getVector() {
        return vector;
    }
}
