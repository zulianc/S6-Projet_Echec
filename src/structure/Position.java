package structure;

public class Position {
     private int x;
     private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static Position getPositionFromIndex(int index) {
        //position = y*8+x <=> x = position%8 && y = position-x/8
        int x = index%8;
        int y = (index-x)/8;
        return new Position(x, y);
    }

    @Override
    public String toString() {
        return "Position [x=" + this.x + ", y=" + this.y + "]";
    }
}
