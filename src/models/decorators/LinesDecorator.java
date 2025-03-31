package models.decorators;

import models.Cell;
import models.ChessBoard;

import java.util.ArrayList;

public class LinesDecorator extends AccessiblesCellsDecorator {
    public LinesDecorator(AccessiblesCellsDecorator base) {
        if (base != null) {
            this.base = base;
        }
    }

    @Override
    protected ArrayList<Cell> getDecoratorCells(ChessBoard chessBoard, Cell startingCell) {
        do {
            Cell next = chessBoard.getCell(startingCell + {0, 1});
        } while (!(next == null) && next.piece == null);
    }
}
