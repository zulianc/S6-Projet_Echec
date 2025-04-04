package models.decorators;

import models.Cell;
import models.ChessBoard;
import structure.Orientation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class KnightDecorator extends AccessibleCellsDecorator{

    public KnightDecorator(AccessibleCellsDecorator base) {
        super(base);
        if (base != null) {
            this.base = base;
        }

        this.orientationPossibles = new ArrayList<>();
        this.orientationPossibles.add(Orientation.LONG_FRONT_LEFT);
        this.orientationPossibles.add(Orientation.LONG_FRONT_RIGHT);
        this.orientationPossibles.add(Orientation.LONG_LEFT_FRONT);
        this.orientationPossibles.add(Orientation.LONG_RIGHT_FRONT);
        this.orientationPossibles.add(Orientation.LONG_LEFT_BACK);
        this.orientationPossibles.add(Orientation.LONG_RIGHT_BACK);
        this.orientationPossibles.add(Orientation.LONG_BACK_LEFT);
        this.orientationPossibles.add(Orientation.LONG_BACK_RIGHT);
    }


    @Override
    protected List<Cell> getAccessibleCellsMess(ChessBoard chessBoard, Cell startingCell) {
        List<Cell> accessibleCells = new LinkedList<>();
        for (Orientation orientation : this.orientationPossibles) {
            Cell nextCell = chessBoard.getCellAtRelativePosition(startingCell, orientation.getVector());

            if (nextCell != null && doesntContainsSameTeamPieces(nextCell, startingCell)) {
                accessibleCells.add(nextCell);
            }
        }

        return accessibleCells;
    }
}
