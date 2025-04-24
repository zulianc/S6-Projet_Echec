package models.decorators;

import models.Cell;
import models.Game;
import models.pieces.Piece;
import structure.Orientation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class KingDecorator extends AccessibleCellsDecorator{
    public KingDecorator(AccessibleCellsDecorator base) {
        super(base);
        this.possibleOrientations = new ArrayList<>();
        this.possibleOrientations.add(Orientation.FRONT);
        this.possibleOrientations.add(Orientation.FRONT_LEFT);
        this.possibleOrientations.add(Orientation.FRONT_RIGHT);
        this.possibleOrientations.add(Orientation.BACK);
        this.possibleOrientations.add(Orientation.BACK_LEFT);
        this.possibleOrientations.add(Orientation.BACK_RIGHT);
        this.possibleOrientations.add(Orientation.LEFT);
        this.possibleOrientations.add(Orientation.RIGHT);
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
