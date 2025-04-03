package models.pieces;

import models.decorators.LinesDecorator;

public class Queen extends Piece {
    public Queen(int team) {
        super(team, new LinesDecorator(null));
    }

    @Override
    public String getPieceName() {
        return "queen";
    }
}
