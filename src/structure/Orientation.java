package structure;

public enum Orientation {
    NULL(            new Position2D( 0,  0)),
    FRONT(           new Position2D( 0, -1)),
    LONG_FRONT(      new Position2D( 0, -2)),
    BACK(            new Position2D( 0,  1)),
    LONG_BACK(       new Position2D( 0,  2)),
    LEFT(            new Position2D(-1,  0)),
    LONG_LEFT(       new Position2D(-2,  0)),
    RIGHT(           new Position2D( 1,  0)),
    LONG_RIGHT(      new Position2D( 2,  0)),
    FRONT_LEFT(      new Position2D(-1, -1)),
    FRONT_RIGHT(     new Position2D( 1, -1)),
    BACK_LEFT(       new Position2D(-1,  1)),
    BACK_RIGHT(      new Position2D( 1,  1));

    private final Position2D vector;

    Orientation(Position2D vector) {
        this.vector = vector;
    }

    public Position2D getVector() {
        return vector;
    }

    @Override
    public String toString() {
        return "Orientation{" + this.vector.toString() + "}";
    }
}
