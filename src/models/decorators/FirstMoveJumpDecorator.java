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
    }

    @Override
    protected List<Cell> getAccessibleCellsMess(ChessBoard chessBoard, Cell startingCell) {
        if (startingCell.hasPiece() && startingCell.getPiece().hasNeverMove()) {

            double playerTeam  = chessBoard.getGame().getActualPlayer().getTeam();
            int totalPlayer    = chessBoard.getGame().getPlayerCount();
            int rotationDegree = (int)((playerTeam / totalPlayer) * 360);
            Position vector = Orientation.rotatingVector(Orientation.FRONT, rotationDegree);


            List<Cell> accessibleCells = new LinkedList<>();
            Cell nextCell = startingCell;
            boolean cellCanMove = true;
            for (int i = 0; i < 2; i++) {
                nextCell = chessBoard.getCellAtRelativePosition(nextCell, vector);

                if (nextCell == null || nextCell.hasPiece()) {
                    cellCanMove = false;
                }
            }
            if (nextCell != null && cellCanMove) {
                accessibleCells.add(nextCell);
            }
            System.out.println(accessibleCells);
            return accessibleCells;
        }

        return new LinkedList<>();
    }
}
