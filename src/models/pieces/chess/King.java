package models.pieces.chess;

import models.decorators.chess.CastlingDecorator;
import models.decorators.chess.KingDecorator;
import models.pieces.Piece;

public class King extends Piece {
    public King(int team) {
        super(team, 10, new KingDecorator(new CastlingDecorator(null)));
    }

    @Override
    public String getPieceName() {
        return "king";
    }

    @Override
    public String getPieceCode() {
        return "K";
    }
}
