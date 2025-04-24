package structure;

public enum Orientation {
    FRONT(           new Position( 0, -1)),
    LONG_FRONT(      new Position( 0, -2)),
    BACK(            new Position( 0,  1)),
    LONG_BACK(       new Position( 0,  2)),
    LEFT(            new Position(-1,  0)),
    LONG_LEFT(       new Position(-2,  0)),
    RIGHT(           new Position( 1,  0)),
    LONG_RIGHT(      new Position( 2,  0)),
    FRONT_LEFT(      new Position(-1, -1)),
    FRONT_RIGHT(     new Position( 1, -1)),
    BACK_LEFT(       new Position(-1,  1)),
    BACK_RIGHT(      new Position( 1,  1));

    private Position vector;

    Orientation(Position vector) {
        this.vector = vector;
    }

    public void add(Orientation orientation) {
        this.vector = new Position(this.vector.getX() + orientation.getVector().getX(), this.vector.getY() + orientation.getVector().getY());
    }

    public void rotate(int actualPlayer, int playerCount) {
        if (playerCount != 2 && playerCount != 4) {
            throw new IllegalArgumentException("Unsupported player count: " + playerCount);
        }

        if ((actualPlayer == 1 && playerCount == 2) || (actualPlayer == 2 && playerCount == 4)) {
            this.rotate180Clockwise();
        }
        if (actualPlayer == 1 && playerCount == 4) {
            this.rotate90Clockwise();
        }
        if (actualPlayer == 3 && playerCount == 4) {
            this.rotate270Clockwise();
        }
    }

    public void rotate90Clockwise() {
        this.vector = new Position(this.vector.getY(), -this.vector.getX());
    }

    public void rotate180Clockwise() {
        this.vector = new Position(-this.vector.getX(), -this.vector.getY());
    }

    public void rotate270Clockwise() {
        this.vector = new Position(-this.vector.getY(), this.vector.getX());
    }

    public Position getVector() {
        return vector;
    }
}
