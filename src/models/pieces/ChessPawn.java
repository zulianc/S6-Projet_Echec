package models.pieces;

public class ChessPawn extends Piece {

    public ChessPawn(int color) {
        super(color, null);
    }

    @Override
    public String getPieceName() {
        return "pawn";
    }
}
