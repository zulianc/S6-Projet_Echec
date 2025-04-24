package models.decorators;

import models.Cell;
import models.Game;
import models.pieces.Piece;
import structure.Orientation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class KnightDecorator extends AccessibleCellsDecorator{
    public KnightDecorator(AccessibleCellsDecorator base) {
        super(base);
        this.possibleOrientations = new ArrayList<>();
        Orientation firstL = Orientation.LONG_FRONT;
        firstL.add(Orientation.LEFT);
        Orientation secondL = Orientation.LONG_FRONT;
        secondL.add(Orientation.RIGHT);

        for (int i = 0; i < 4; i++) {
            firstL.rotate90Clockwise();
            secondL.rotate90Clockwise();
            this.possibleOrientations.add(firstL);
            this.possibleOrientations.add(secondL);
        }
    }

    @Override
    protected List<Cell> getDecoratorAccessibleCells(Game game, Piece piece) {
        List<Cell> accessibleCells = new LinkedList<>();

        Cell startingCell = game.getBoard().getCellOfPiece(piece);

        for (Orientation orientation : this.possibleOrientations) {
            Cell nextCell = game.getBoard().getCellAtRelativePosition(startingCell, orientation.getVector());

            if (nextCell != null && containsPiecesOfDifferentTeams(nextCell, startingCell)) {
                accessibleCells.add(nextCell);
            }
        }

        return accessibleCells;
    }
}
