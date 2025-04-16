package models.pieces;

import models.decorators.LinesDecorator;

public class Rook extends Piece {
    public Rook(int team) {
        super(team, new LinesDecorator(null), 5);
    }

    @Override
    public String getPieceName() {
        return "rook";
    }
}
