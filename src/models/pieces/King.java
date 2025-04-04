package models.pieces;

import models.decorators.KingDecorator;

public class King extends Piece {

    public King(int team) {
        super(team, new KingDecorator(null));
    }

    @Override
    public String getPieceName() {
        return "king";
    }
}
