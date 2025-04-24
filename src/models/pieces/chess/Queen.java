package models.pieces.chess;

import models.decorators.chess.DiagonalsDecorator;
import models.decorators.chess.LinesDecorator;
import models.pieces.Piece;

public class Queen extends Piece {
    public Queen(int team) {
        super(team, 9, new LinesDecorator(new DiagonalsDecorator(null)));
    }

    @Override
    public String getPieceName() {
        return "queen";
    }
}
