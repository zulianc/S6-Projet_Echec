package models.pieces;

import models.decorators.KnightDecorator;

public class Knight extends Piece {
    public Knight(int team) {
        super(team, 3, new KnightDecorator(null));
    }

    @Override
    public String getPieceName() {
        return "knight";
    }
}
