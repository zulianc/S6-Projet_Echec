package models.pieces.chess;

import models.decorators.chess.KnightDecorator;
import models.pieces.Piece;

public class Knight extends Piece {
    public Knight(int team) {
        super(team, 3, new KnightDecorator(null));
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
