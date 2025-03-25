package models.pieces;

public abstract class Piece {
    private final int color;

    public Piece(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public abstract String getPieceName() ;

    @Override
    public String toString() {
        return "Piece{" +
                "color=" + color +
                '}';
    }
}
