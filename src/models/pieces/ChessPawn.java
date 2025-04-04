package models.pieces;

import models.decorators.ChessPawnDecorator;
import models.decorators.LinesDecorator;

public class ChessPawn extends Piece {

    public ChessPawn(int team) {
        super(team, new ChessPawnDecorator(null));
    }

    @Override
    public String getPieceName() {
        return "pawn";
    }
}
