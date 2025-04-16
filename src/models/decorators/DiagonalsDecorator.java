package models.decorators;

import models.Cell;
import models.ChessBoard;
import structure.Orientation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DiagonalsDecorator extends AccessibleCellsDecorator {
    public DiagonalsDecorator(AccessibleCellsDecorator base) {
        super(base);
        this.orientationPossibles = new ArrayList<>();
        this.orientationPossibles.add(Orientation.FRONT_LEFT);
        this.orientationPossibles.add(Orientation.FRONT_RIGHT);
        this.orientationPossibles.add(Orientation.BACK_LEFT);
        this.orientationPossibles.add(Orientation.BACK_RIGHT);
    }

    @Override
    protected List<Cell> getDecoratorAccessibleCells(ChessBoard chessBoard, Cell startingCell) {
        List<Cell> accessibleCells = new LinkedList<>();
        for (Orientation orientation : this.orientationPossibles) {
            Cell nextCell = startingCell;
            boolean pieceIsBlocked = false;
            while (!pieceIsBlocked) {
                nextCell = chessBoard.getCellAtRelativePosition(nextCell, orientation.getVector());

                if (nextCell == null) {
                    pieceIsBlocked = true;
                } else if (nextCell.hasPiece()) {
                    if (nextCell.getPiece().getTeam() != startingCell.getPiece().getTeam()) {
                        accessibleCells.add(nextCell);
                    }
                    pieceIsBlocked = true;
                } else {
                    accessibleCells.add(nextCell);
                }
            }
        }

        return accessibleCells;
    }
}
