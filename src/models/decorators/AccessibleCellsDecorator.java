package models.decorators;

import models.Cell;
import models.ChessBoard;
import models.pieces.ChessPawn;
import structure.Orientation;

import java.util.List;

public abstract class AccessibleCellsDecorator {
    protected AccessibleCellsDecorator base;
    protected List<Orientation> orientationPossibles;

    protected AccessibleCellsDecorator(AccessibleCellsDecorator base) {
        this.base = base;
    }

    protected abstract List<Cell> getDecoratorAccessibleCells(ChessBoard chessBoard, Cell startingCell);

    public List<Cell> getAccessibleCells(ChessBoard chessBoard, Cell startingCell) {
        List<Cell> cells = this.getDecoratorAccessibleCells(chessBoard, startingCell);
        AccessibleCellsDecorator baseDecorator = this.base;
        while (baseDecorator != null) {
            cells.addAll(baseDecorator.getDecoratorAccessibleCells(chessBoard, startingCell));
            baseDecorator = baseDecorator.base;
        }
        return cells;
    }

    protected boolean doesntContainsSameTeamPieces(Cell cell1, Cell cell2) {
        return !cell1.hasPiece() || cell1.getPiece().getTeam() != cell2.getPiece().getTeam();
    }
}
