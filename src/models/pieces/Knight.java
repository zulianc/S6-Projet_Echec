package models.pieces;

import models.decorators.KnightDecorator;

public class Knight extends Piece {
    public Knight(int team) {
        super(team, new KnightDecorator(null), 3);
    }

    @Override
    public String getPieceName() {
        return "knight";
    }

    @Override
    public String getPieceCode() {
        return "N";
    }
}
