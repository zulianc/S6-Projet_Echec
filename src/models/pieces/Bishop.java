package models.pieces;

import models.decorators.DiagonalsDecorator;

public class Bishop extends Piece {

    public Bishop(int team) {
        super(team, new DiagonalsDecorator(null));
    }

    @Override
    public String getPieceName() {
        return "bishop";
    }
}
