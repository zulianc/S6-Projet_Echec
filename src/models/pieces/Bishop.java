package models.pieces;

public class Bishop extends Piece {

    public Bishop(int color) {
        super(color);
    }

    @Override
    public String getPieceName() {
        return "bishop";
    }
}
