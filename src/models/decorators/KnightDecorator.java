package models.decorators;

import models.Cell;
import models.ChessBoard;

import java.util.List;

public class KnightDecorator extends AccessibleCellsDecorator{

    public KnightDecorator(AccessibleCellsDecorator base) {
        super(base);
        if (base != null) {
            this.base = base;
        }
    }


    @Override
    protected List<Cell> getAccessibleCellsMess(ChessBoard chessBoard, Cell startingCell) {
        return null;
    }
}
