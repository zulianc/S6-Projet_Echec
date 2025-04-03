package models.pieces;

public class Knight extends Piece {

    public Knight(int team) {
        super(team, null);
    }

    @Override
    public String getPieceName() {
        return "knight";
    }
}
