package models.decorators;

import models.Cell;
import models.Game;
import models.pieces.Piece;
import structure.Orientation;

import java.util.LinkedList;
import java.util.List;

public abstract class AccessibleCellsDecorator {
    protected AccessibleCellsDecorator base;
    protected List<Orientation> possibleOrientations;

    protected AccessibleCellsDecorator(AccessibleCellsDecorator base) {
        this.base = base;
    }

    protected abstract List<Cell> getDecoratorAccessibleCells(Game game, Piece piece);

    public List<Cell> getAccessibleCells(Game game, Piece piece) {
        if (piece == null) {
            throw new RuntimeException("decorator: piece is null");
        }

        List<Cell> cells = new LinkedList<>();
        AccessibleCellsDecorator baseDecorator = this;
        while (baseDecorator != null) {
            cells.addAll(baseDecorator.getDecoratorAccessibleCells(game, piece));
            baseDecorator = baseDecorator.base;
        }
        return cells;
    }

    protected boolean containsPiecesOfDifferentTeams(Cell cell1, Cell cell2) {
        return cell1.hasPiece() && cell2.hasPiece() && cell1.getPiece().getTeam() != cell2.getPiece().getTeam();
    }

    protected boolean containsPiecesOfSameTeams(Cell cell1, Cell cell2) {
        return cell1.hasPiece() && cell2.hasPiece() && cell1.getPiece().getTeam() == cell2.getPiece().getTeam();
    }
}
