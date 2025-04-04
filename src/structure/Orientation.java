package structure;

public enum Orientation {
    FRONT(new Position(0, -1)),
    BACK(new Position(0, 1)),
    LEFT(new Position(-1, 0)),
    RIGHT(new Position(1, 0)),
    FRONT_LEFT(new Position(-1, -1)),
    FRONT_RIGHT(new Position(1, -1)),
    BACK_LEFT(new Position(-1, 1)),
    BACK_RIGHT(new Position(1, 1)),
    LONG_FRONT_LEFT(new Position(-2, -1)),
    LONG_FRONT_RIGHT(new Position(-2, 1)),
    LONG_LEFT_FRONT(new Position(-1, -2)),
    LONG_RIGHT_FRONT(new Position(-1, 2)),
    LONG_LEFT_BACK(new Position(1, -2)),
    LONG_RIGHT_BACK(new Position(1, 2)),
    LONG_BACK_LEFT(new Position(2, -1)),
    LONG_BACK_RIGHT(new Position(2, 1));


    private final Position vector;

    Orientation(Position vector) {
        this.vector = vector;
    }

    public static Position getVectorRotatedBy90Degrees(Orientation orientation) {
        Position vector = orientation.getVector();
        return new Position(vector.getX()*-1, vector.getY()*-1);
    }

    public Position getVector() {
        return vector;
    }
}
