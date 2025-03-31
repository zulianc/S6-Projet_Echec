package models.pieces;

public class Knight extends Piece {

    public Knight(int color) {
        super(color, null);
    }

    @Override
    public String getPieceName() {
        return "knight";
    }
}
