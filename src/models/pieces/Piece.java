package models.pieces;

public abstract class Piece {

    private static final PieceName PIECE_NAME = PieceName.PIECE;
    private final int color;

    public Piece(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public String getPieceName() {
        return PIECE_NAME.getValue();
    }
}
