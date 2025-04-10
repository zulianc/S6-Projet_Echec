package models.decorators;

import models.Cell;
import models.ChessBoard;
import structure.Orientation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CastlingDecorator extends AccessibleCellsDecorator {

    public CastlingDecorator (AccessibleCellsDecorator base) {
        super(null);
        if (base != null) {
            this.base = base;
        }
        this.orientationPossibles = new ArrayList<>();
        this.orientationPossibles.add(Orientation.LEFT);
        this.orientationPossibles.add(Orientation.RIGHT);
    }

    @Override
    protected List<Cell> getAccessibleCellsMess(ChessBoard chessBoard, Cell startingCell) {
        List<Cell> accessibleCells = new LinkedList<>();
        if (startingCell.hasPiece() && !startingCell.getPiece().hasAlreadyMove()) {

            for (Orientation orientation : this.orientationPossibles) {
                Cell previousCell;
                Cell nextCell = startingCell;
                boolean pieceIsBlocked = false;

                while (!pieceIsBlocked) {
                    previousCell = nextCell;
                    nextCell = chessBoard.getCellAtRelativePosition(nextCell, orientation.getVector());

                    if (nextCell == null) {
                        pieceIsBlocked = true;
                    } else if (nextCell.hasPiece()) {
                        if (cellHasRockWithItCanDoCastling(startingCell, nextCell)) {
                            accessibleCells.add(previousCell);
                        }
                        pieceIsBlocked = true;
                    }
                }
            }
        }

        return accessibleCells;
    }

    private boolean cellHasRockWithItCanDoCastling(Cell startingCell, Cell cellToTest) {
        return cellToTest.hasPiece() && !doesntContainsSameTeamPieces(startingCell, cellToTest) && cellToTest.getPiece().getPieceName().equals("rook") && !cellToTest.getPiece().hasAlreadyMove();
    }
}
