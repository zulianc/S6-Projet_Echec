package models.pieces;

public class Rook extends Piece {

    public Rook(int color) {
        super(color);
    }

    @Override
    public String getPieceName() {
        return "rook";
    }

}
