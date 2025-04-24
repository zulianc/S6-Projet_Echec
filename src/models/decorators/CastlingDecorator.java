package models.decorators;

import models.Cell;
import models.ChessBoard;
import models.Move;
import structure.Orientation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CastlingDecorator extends AccessibleCellsDecorator {
    public CastlingDecorator (AccessibleCellsDecorator base) {
        super(base);
        this.orientationPossibles = new ArrayList<>();
        this.orientationPossibles.add(Orientation.LEFT);
        this.orientationPossibles.add(Orientation.RIGHT);
    }

    @Override
    protected List<Cell> getDecoratorAccessibleCells(ChessBoard chessBoard, Cell startingCell) {
        List<Cell> accessibleCells = new LinkedList<>();
        if (startingCell.hasPiece() && startingCell.getPiece().hasNeverMoved()) {
            if (startingCell.getPiece().getTeam() == chessBoard.getGame().getActualPlayer().getTeam()) {
                for (Orientation orientation : this.orientationPossibles) {
                    Cell cellPassingBy = chessBoard.getCellAtRelativePosition(startingCell, orientation.getVector());
                    Cell cellStoppingAt = chessBoard.getCellAtRelativePosition(cellPassingBy, orientation.getVector());

                    Cell rookCell = cellStoppingAt;
                    boolean canCastle = false;
                    boolean endOfBoard = false;
                    do {
                        rookCell = chessBoard.getCellAtRelativePosition(rookCell, orientation.getVector());
                        if (rookCell == null) {
                            endOfBoard = true;
                        } else {
                            canCastle = cellHasRockWithItCanDoCastling(startingCell, rookCell);
                        }
                    } while (!canCastle && !endOfBoard);

                    if (canCastle) {
                        if (cellPassingBy != null && cellStoppingAt != null) {
                            if (cellPassingBy.getPiece() == null && cellStoppingAt.getPiece() == null) {
                                Move goToStartingCell   = new Move(startingCell, startingCell);
                                Move goToPassingByCell  = new Move(startingCell, cellPassingBy);
                                Move goToStoppingAtCell = new Move(startingCell, cellStoppingAt);

                                if (notInCheck(chessBoard, goToStartingCell, goToPassingByCell, goToStoppingAtCell)) {
                                    accessibleCells.add(cellStoppingAt);
                                }
                            }
                        }
                    }
                }
            }
        }

        return accessibleCells;
    }

    private boolean cellHasRockWithItCanDoCastling(Cell startingCell, Cell cellToTest) {
        return cellToTest.hasPiece() && !doesntContainsSameTeamPieces(startingCell, cellToTest) && cellToTest.getPiece().getPieceName().equals("rook") && cellToTest.getPiece().hasNeverMoved();
    }

    private boolean notInCheck(ChessBoard chessBoard, Move goToStartingCell, Move goToPassingByCell, Move goToStoppingAtCell) {
        return !chessBoard.getGame().isInCheckIfMove(goToStartingCell) && !chessBoard.getGame().isInCheckIfMove(goToPassingByCell) && !chessBoard.getGame().isInCheckIfMove(goToStoppingAtCell);
    }
}
