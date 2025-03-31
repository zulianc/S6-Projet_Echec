package models.pieces;

public class Rook extends Piece {

    public Rook(int color) {
        super(color, null);
    }

    @Override
    public String getPieceName() {
        return "rook";
    }

}
