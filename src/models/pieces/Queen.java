package models.pieces;

import models.decorators.DiagonalsDecorator;
import models.decorators.LinesDecorator;

public class Queen extends Piece {
    public Queen(int team) {
        super(team, 9, new LinesDecorator(new DiagonalsDecorator(null)));
    }

    @Override
    public String getPieceName() {
        return "queen";
    }
}
