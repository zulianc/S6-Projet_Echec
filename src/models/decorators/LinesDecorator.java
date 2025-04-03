package models.decorators;

import models.Cell;
import models.ChessBoard;
import structure.Position;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LinesDecorator extends AccessibleCellsDecorator {
    public LinesDecorator(AccessibleCellsDecorator base) {
        if (base != null) {
            this.base = base;
        }
    }

    @Override
    protected List<Cell> getAccessibleCellsMess(ChessBoard chessBoard, Cell startingCell) {
        List<Cell> accessibleCells = new LinkedList<>();
        Cell nextCell;
        do {
            nextCell = chessBoard.getCellAtRelativePosition(startingCell, new Position(0, 1));
            accessibleCells.add(nextCell);
        } while (!(nextCell == null) && !nextCell.hasPiece());

        return accessibleCells;
    }
}
