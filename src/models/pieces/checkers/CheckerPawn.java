package models.pieces.checkers;

import models.decorators.checkers.CheckerPawnDecorator;
import models.pieces.Piece;

public class CheckerPawn extends Piece {
    public CheckerPawn(int team) {
        super(team, 1, new CheckerPawnDecorator(null));
    }

    @Override
    public String getPieceName() {
        return "pawn";
    }

    @Override
    public String getPieceCode() {
        return "";
    }
}
