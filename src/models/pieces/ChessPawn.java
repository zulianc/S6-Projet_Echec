package models.pieces;

import models.decorators.ChessPawnDecorator;
import models.decorators.EnPassantDecorator;
import models.decorators.FirstMoveJumpDecorator;

public class ChessPawn extends Piece {
    public ChessPawn(int team) {
        super(team, 1, new ChessPawnDecorator(new FirstMoveJumpDecorator(new EnPassantDecorator(null))));
    }

    @Override
    public String getPieceName() {
        return "pawn";
    }
}
