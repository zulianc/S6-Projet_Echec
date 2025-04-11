package models.pieces;

import models.decorators.ChessPawnDecorator;
import models.decorators.FirstMoveJumpDecorator;

public class ChessPawn extends Piece {

    public ChessPawn(int team) {
        super(team, new ChessPawnDecorator(new FirstMoveJumpDecorator(null)));
    }

    @Override
    public String getPieceName() {
        return "pawn";
    }
}
