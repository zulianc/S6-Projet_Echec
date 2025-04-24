package models.pieces.chess;

import models.decorators.chess.ChessPawnDecorator;
import models.decorators.chess.EnPassantDecorator;
import models.decorators.chess.FirstMoveJumpDecorator;
import models.pieces.Piece;

public class ChessPawn extends Piece {
    public ChessPawn(int team) {
        super(team, 1, new ChessPawnDecorator(new FirstMoveJumpDecorator(new EnPassantDecorator(null))));
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
