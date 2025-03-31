package models.pieces;

public class Bishop extends Piece {

    public Bishop(int color) {
        super(color, null);
    }

    @Override
    public String getPieceName() {
        return "bishop";
    }
}
