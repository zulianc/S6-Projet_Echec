package models.pieces;

public class King extends Piece {

    public King(int team) {
        super(team, null);
    }

    @Override
    public String getPieceName() {
        return "king";
    }
}
