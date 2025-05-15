package structure;

public class Position2D {
     private final int x;
     private final int y;

    public Position2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position2D copy() {
        return new Position2D(x, y);
    }

    public Position2D add(Position2D position) {
        return new Position2D(this.x + position.x, this.y + position.y);
    }

    public Position2D rotate(int actualPlayer, int playerCount) {
        if (playerCount != 2 && playerCount != 4) {
            throw new IllegalArgumentException("Unsupported player count: " + playerCount);
        }

        if ((actualPlayer == 1 && playerCount == 2) || (actualPlayer == 2 && playerCount == 4)) {
            return this.rotate180Clockwise();
        }
        if (actualPlayer == 1 && playerCount == 4) {
            return this.rotate90Clockwise();
        }
        if (actualPlayer == 3 && playerCount == 4) {
            return this.rotate270Clockwise();
        }

        return this.copy();
    }

    public Position2D rotate90Clockwise() {
        return new Position2D(this.y, -this.x);
    }

    public Position2D rotate180Clockwise() {
        return new Position2D(-this.x, -this.y);
    }

    public Position2D rotate270Clockwise() {
        return new Position2D(-this.y, this.x);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static Position2D getPositionFromIndex(int index, int boardWidth) {
        int x = index % boardWidth;
        int y = (index - x) / boardWidth;
        return new Position2D(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Position2D vector = (Position2D) o;
        return this.x == vector.x && this.y == vector.y;
    }

    @Override
    public String toString() {
        return "Position{x=" + this.x + ", y=" + this.y + "}";
    }
}
