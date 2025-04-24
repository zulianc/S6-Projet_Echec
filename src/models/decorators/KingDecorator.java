package models.decorators;

import models.Cell;
import models.Game;
import models.pieces.Piece;
import structure.Orientation;
import structure.Position2D;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class KingDecorator extends AccessibleCellsDecorator{
    public KingDecorator(AccessibleCellsDecorator base) {
        super(base);
        this.possibleVectors = new ArrayList<>();
        this.possibleVectors.add(Orientation.FRONT.getVector());
        this.possibleVectors.add(Orientation.FRONT_LEFT.getVector());
        this.possibleVectors.add(Orientation.FRONT_RIGHT.getVector());
        this.possibleVectors.add(Orientation.BACK.getVector());
        this.possibleVectors.add(Orientation.BACK_LEFT.getVector());
        this.possibleVectors.add(Orientation.BACK_RIGHT.getVector());
        this.possibleVectors.add(Orientation.LEFT.getVector());
        this.possibleVectors.add(Orientation.RIGHT.getVector());
    }

    @Override
    protected List<Cell> getDecoratorAccessibleCells(Game game, Piece piece) {
        List<Cell> accessibleCells = new LinkedList<>();

        Cell startingCell = game.getBoard().getCellOfPiece(piece);

        for (Position2D vector : this.possibleVectors) {
            Cell nextCell = game.getBoard().getCellAtRelativePosition(startingCell, vector);

            if (nextCell != null && !this.containsPiecesOfSameTeams(nextCell, startingCell)) {
                accessibleCells.add(nextCell);
            }
        }

        return accessibleCells;
    }
}
