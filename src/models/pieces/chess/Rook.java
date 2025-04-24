package models.pieces.chess;

import models.decorators.chess.LinesDecorator;
import models.pieces.Piece;

public class Rook extends Piece {
    public Rook(int team) {
        super(team, 5, new LinesDecorator(null));
    }

    @Override
    public String getPieceName() {
        return "rook";
    }
}
