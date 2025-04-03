package models.pieces;

public class Bishop extends Piece {

    public Bishop(int team) {
        super(team, null);
    }

    @Override
    public String getPieceName() {
        return "bishop";
    }
}
