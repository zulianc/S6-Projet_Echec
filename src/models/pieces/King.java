package models.pieces;

public class King extends Piece {

    public King(int color) {
        super(color, null);
    }

    @Override
    public String getPieceName() {
        return "king";
    }
}
