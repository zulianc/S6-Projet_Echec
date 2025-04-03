package models.decorators;

import models.Cell;
import models.ChessBoard;

import java.util.ArrayList;
import java.util.List;

public abstract class AccessibleCellsDecorator {
    AccessibleCellsDecorator base;

    protected abstract List<Cell> getAccessibleCellsMess(ChessBoard chessBoard, Cell startingCell);

    public List<Cell> getAccessibleCells(ChessBoard chessBoard, Cell startingCell) {
        List<Cell> cells = getAccessibleCellsMess(chessBoard, startingCell);
        if (base != null) {
            cells.addAll(base.getAccessibleCellsMess(chessBoard, startingCell));
        }

        return cells;
    }
}
