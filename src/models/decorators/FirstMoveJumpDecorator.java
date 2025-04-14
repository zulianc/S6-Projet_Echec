package models.decorators;

import models.Cell;
import models.ChessBoard;
import structure.Orientation;
import structure.Position;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FirstMoveJumpDecorator extends AccessibleCellsDecorator {

    public FirstMoveJumpDecorator (AccessibleCellsDecorator base) {
        super(null);
        if (base != null) {
            this.base = base;
        }
        this.orientationPossibles = new ArrayList<>();
        this.orientationPossibles.add(Orientation.LONG_FRONT);
    }

    @Override
    protected List<Cell> getAccessibleCellsMess(ChessBoard chessBoard, Cell startingCell) {
        if (startingCell.hasPiece() && startingCell.getPiece().hasNeverMove()) {

            List<Position> orientationVectors = new LinkedList<>();
            for (Orientation orientation : this.orientationPossibles) {
                double playerTeam  = chessBoard.getGame().getActualPlayer().getTeam();
                int totalPlayer    = chessBoard.getGame().getPlayerCount();
                int rotationDegree = (int)((playerTeam / totalPlayer) * 360);
                orientationVectors.add(Orientation.rotatingVector(orientation, rotationDegree));
            }


            List<Cell> accessibleCells = new LinkedList<>();
            for (Position vector : orientationVectors) {
                Cell nextCell = chessBoard.getCellAtRelativePosition(startingCell, vector);

                if (nextCell != null && !nextCell.hasPiece()) {
                    accessibleCells.add(nextCell);
                }
            }
            return accessibleCells;
        }

        return new LinkedList<>();
    }
}
