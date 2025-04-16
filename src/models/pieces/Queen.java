package models.pieces;

import models.decorators.DiagonalsDecorator;
import models.decorators.LinesDecorator;

public class Queen extends Piece {
    public Queen(int team) {
        super(team, new LinesDecorator(new DiagonalsDecorator(null)), 9);
    }

    @Override
    public String getPieceName() {
        return "queen";
    }
}
