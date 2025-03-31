package models.pieces;

import models.decorators.AccessiblesCellsDecorator;
import models.decorators.LinesDecorator;

public class Queen extends Piece {
    public Queen(int color) {
        super(color, new LinesDecorator(null));
    }

    @Override
    public String getPieceName() {
        return "queen";
    }
}
