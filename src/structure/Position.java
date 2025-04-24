package structure;

public class Position {
     private final int x;
     private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static Position getPositionFromIndex(int index, int boardWidth, int boardHeight) {
        int x = index % boardWidth;
        int y = (index - x) / boardWidth;
        return new Position(x, y);
    }

    @Override
    public String toString() {
        return "Position{x=" + this.x + ", y=" + this.y + "}";
    }
}
