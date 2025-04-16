package models.pieces;

import models.decorators.DiagonalsDecorator;

public class Bishop extends Piece {
    public Bishop(int team) {
        super(team, new DiagonalsDecorator(null), 3);
    }

    @Override
    public String getPieceName() {
        return "bishop";
    }
}
