package models.decorators;

import models.Cell;
import models.ChessBoard;
import structure.Orientation;
import structure.Position;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChessPawnDecorator extends AccessibleCellsDecorator{
    public ChessPawnDecorator(AccessibleCellsDecorator base) {
        super(null);
        if (base != null) {
            this.base = base;
        }
        this.orientationPossibles = new ArrayList<>();
        this.orientationPossibles.add(Orientation.FRONT_LEFT);
        this.orientationPossibles.add(Orientation.FRONT_RIGHT);
    }

    @Override
    protected List<Cell> getAccessibleCellsMess(ChessBoard chessBoard, Cell startingCell) {
        List<Position> orientationVectors = new LinkedList<>();
        double playerTeam  = chessBoard.getGame().getActualPlayer().getTeam();
        int totalPlayer    = chessBoard.getGame().getPlayerCount();
        int rotationDegree = (int)((playerTeam / totalPlayer) * 360);
        for (Orientation orientation : this.orientationPossibles) {
            orientationVectors.add(Orientation.rotatingVector(orientation, rotationDegree));
        }


        List<Cell> accessibleCells = new LinkedList<>();
        for (Position vector : orientationVectors) {
            Cell nextCell = chessBoard.getCellAtRelativePosition(startingCell, vector);

            if (nextCell != null && nextCell.hasPiece() && doesntContainsSameTeamPieces(nextCell, startingCell)) {
                accessibleCells.add(nextCell);
            }
        }

        Cell nextCell = chessBoard.getCellAtRelativePosition(startingCell, Orientation.rotatingVector(Orientation.FRONT, rotationDegree));

        if (nextCell != null && !nextCell.hasPiece()) {
            accessibleCells.add(nextCell);
        }

        return accessibleCells;
    }


}
