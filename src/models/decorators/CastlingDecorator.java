package models.decorators;

import models.Cell;
import models.ChessBoard;
import models.Move;
import structure.Orientation;
import structure.Position;

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
                                Move goToStartingCell = new Move(chessBoard.getPositionOfCell(startingCell), chessBoard.getPositionOfCell(startingCell));
                                Move goToPassingByCell = new Move(chessBoard.getPositionOfCell(startingCell), chessBoard.getPositionOfCell(cellPassingBy));
                                Move goToStoppingAtCell = new Move(chessBoard.getPositionOfCell(startingCell), chessBoard.getPositionOfCell(cellStoppingAt));

                                boolean notInCheck = !chessBoard.getGame().isInCheckIfMove(goToStartingCell) && !chessBoard.getGame().isInCheckIfMove(goToPassingByCell) && !chessBoard.getGame().isInCheckIfMove(goToStoppingAtCell);

                                if (notInCheck) {
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
        return cellToTest.hasPiece() && !doesntContainsSameTeamPieces(startingCell, cellToTest) && cellToTest.getPiece().getPieceName().equals("rook") && !cellToTest.getPiece().hasAlreadyMove();
    }
}
