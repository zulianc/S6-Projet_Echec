package models.decorators;

import models.Cell;
import models.ChessBoard;

import java.util.List;

public class EnPassantDecorator extends AccessibleCellsDecorator {
    protected EnPassantDecorator(AccessibleCellsDecorator base) {
        super(base);
    }

    @Override
    protected List<Cell> getAccessibleCellsMess(ChessBoard chessBoard, Cell startingCell) {
        return List.of();
    }
}
