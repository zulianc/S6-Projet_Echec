package models.decorators;

import models.Cell;
import models.ChessBoard;

import java.util.ArrayList;

public abstract class AccessiblesCellsDecorator {
    AccessiblesCellsDecorator base;

    protected abstract ArrayList<Cell> getDecoratorCells(ChessBoard chessBoard, Cell startingCell);

    public ArrayList<Cell> getAccessiblesCells(ChessBoard chessBoard, Cell startingCell) {
        ArrayList<Cell> cells = getDecoratorCells(chessBoard, startingCell);
        if (base != null) {
            cells.addAll(base.getDecoratorCells(chessBoard, startingCell));
        }

        return cells;
    }
}
