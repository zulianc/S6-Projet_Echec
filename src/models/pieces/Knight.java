package models.pieces;

public class Knight extends Piece {

    public Knight(int color) {
        super(color);
    }

    @Override
    public String getPieceName() {
        return "knight";
    }
}
