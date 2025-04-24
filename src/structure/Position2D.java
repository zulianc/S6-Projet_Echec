package structure;

public class Position2D {
     private int x;
     private int y;

    public Position2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position2D copy() {
        return new Position2D(x, y);
    }

    public void add(Position2D position) {
        this.x += position.x;
        this.y += position.y;
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
        int memX = this.x;
        this.x = +this.y;
        this.y = -memX;
    }

    public void rotate180Clockwise() {
        this.x = -this.x;
        this.y = -this.y;
    }

    public void rotate270Clockwise() {
        int memX = this.x;
        this.x = -this.y;
        this.y = +memX;
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
