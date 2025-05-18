package models.pieces.checkers;

import models.decorators.checkers.CheckerQueenDecorator;
import models.pieces.Piece;

public class CheckerQueen extends Piece {
    public CheckerQueen(int team) {
        super(team, 10, new CheckerQueenDecorator(null));
    }

    @Override
    public String getPieceName() {
        return "queen";
    }

    @Override
    public String getPieceCode() {
        return "";
    }
}
