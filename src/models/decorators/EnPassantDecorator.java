package models.decorators;

import models.Cell;
import models.ChessBoard;
import models.Move;
import models.pieces.ChessPawn;
import structure.Orientation;
import structure.Position;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EnPassantDecorator extends AccessibleCellsDecorator {
    public EnPassantDecorator(AccessibleCellsDecorator base) {
        super(base);
        this.orientationPossibles = new ArrayList<>();
        this.orientationPossibles.add(Orientation.FRONT_LEFT);
        this.orientationPossibles.add(Orientation.FRONT_RIGHT);
    }

    @Override
    protected List<Cell> getDecoratorAccessibleCells(ChessBoard chessBoard, Cell startingCell) {
        List<Position> orientationVectors = new LinkedList<>();
        int playerTeam     = chessBoard.getGame().getActualPlayer().getTeam();
        int totalPlayer    = chessBoard.getGame().getPlayerCount();
        int rotationDegree = ((playerTeam * 360) / totalPlayer);
        for (Orientation orientation : this.orientationPossibles) {
            orientationVectors.add(Orientation.rotatingVector(orientation, rotationDegree));
        }

        List<Cell> accessibleCells = new LinkedList<>();
        if (startingCell.getPiece().getTeam() == chessBoard.getGame().getActualPlayer().getTeam()) {
            for (Position vector : orientationVectors) {
                Position backVector = Orientation.rotatingVector(Orientation.BACK, rotationDegree);
                Cell cellToMoveAt = chessBoard.getCellAtRelativePosition(startingCell, vector);
                Cell cellWherePawnIs = chessBoard.getCellAtRelativePosition(cellToMoveAt, backVector);
                if (cellToMoveAt != null && cellWherePawnIs != null && cellWherePawnIs.getPiece() != null) {
                    if (this.doesntContainsSameTeamPieces(startingCell, cellWherePawnIs)) {
                        if (cellWherePawnIs.getPiece() instanceof ChessPawn) {
                            if (cellWherePawnIs.getPiece().getMoveCount() == 1) {
                                if (cellWherePawnIs.getPiece().getLastMoveTurn() > chessBoard.getGame().getTurn() - chessBoard.getGame().getPlayerCount()) {
                                    accessibleCells.add(cellToMoveAt);
                                }
                            }
                        }
                    }
                }
            }
        }

        return accessibleCells;
    }
}
