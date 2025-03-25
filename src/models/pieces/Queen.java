package models.pieces;

public class Queen extends Piece {

    public Queen(int color) {
        super(color);
    }

    @Override
    public String getPieceName() {
        return "queen";
    }
}
