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
        for (Orientation orientation : this.orientationPossibles) {
            if (startingCell.getPiece().getTeam() == 1) {
                orientationVectors.add(Orientation.getVectorRotatedBy180Degrees(orientation));
            }
            orientationVectors.add(orientation.getVector());
        }


        List<Cell> accessibleCells = new LinkedList<>();
        for (Position vector : orientationVectors) {
            Cell nextCell = chessBoard.getCellAtRelativePosition(startingCell, vector);

            if (nextCell != null && nextCell.hasPiece() && doesntContainsSameTeamPieces(nextCell, startingCell)) {
                accessibleCells.add(nextCell);
            }
        }

        Cell nextCell = startingCell.getPiece().getTeam() == 1 ? chessBoard.getCellAtRelativePosition(startingCell, Orientation.getVectorRotatedBy180Degrees(Orientation.FRONT)) : chessBoard.getCellAtRelativePosition(startingCell, Orientation.FRONT.getVector());

        if (nextCell != null ) {
            if (!nextCell.hasPiece()) {
                accessibleCells.add(nextCell);
            }
        } else {
            //TODO promote ?
        }

        return accessibleCells;
    }


}
