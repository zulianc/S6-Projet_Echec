package models.pieces;

import models.decorators.CastlingDecorator;
import models.decorators.KingDecorator;

public class King extends Piece {
    public King(int team) {
        super(team, new KingDecorator(new CastlingDecorator(null)), 10);
    }

    @Override
    public String getPieceName() {
        return "king";
    }
}
