package models.pieces.chess;

import models.decorators.chess.DiagonalsDecorator;
import models.pieces.Piece;

public class Bishop extends Piece {
    public Bishop(int team) {
        super(team, 3, new DiagonalsDecorator(null));
    }

    @Override
    public String getPieceName() {
        return "bishop";
    }

    @Override
    public String getPieceCode() {
        return "B";
    }
}
