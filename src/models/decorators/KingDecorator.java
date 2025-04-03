package models.decorators;

import models.Cell;
import models.ChessBoard;
import structure.Orientation;

import java.util.ArrayList;
import java.util.List;

public class KingDecorator extends AccessibleCellsDecorator{
    public KingDecorator(AccessibleCellsDecorator base) {
        super(null);
        if (base != null) {
            this.base = base;
        }
        this.orientationPossibles = new ArrayList<>();
        this.orientationPossibles.add(Orientation.FRONT);
        this.orientationPossibles.add(Orientation.FRONT_LEFT);
        this.orientationPossibles.add(Orientation.FRONT_RIGHT);
        this.orientationPossibles.add(Orientation.BACK);
        this.orientationPossibles.add(Orientation.BACK_LEFT);
        this.orientationPossibles.add(Orientation.BACK_RIGHT);
    }

    @Override
    protected List<Cell> getAccessibleCellsMess(ChessBoard chessBoard, Cell startingCell) {
        return null;
    }
}
