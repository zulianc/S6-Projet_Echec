package models.decorators.chess;

import models.boards.Cell;
import models.decorators.AccessibleCellsDecorator;
import models.games.Game;
import models.pieces.Piece;
import structure.Orientation;
import structure.Position2D;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class KnightDecorator extends AccessibleCellsDecorator {
    public KnightDecorator(AccessibleCellsDecorator base) {
        super(base);

        this.possibleVectors = new ArrayList<>();

        Position2D firstL = Orientation.LONG_FRONT.getVector();
        firstL.add(Orientation.LEFT.getVector());

        Position2D secondL = Orientation.LONG_FRONT.getVector();
        secondL.add(Orientation.RIGHT.getVector());

        for (int i = 0; i < 4; i++) {
            this.possibleVectors.add(firstL.copy());
            this.possibleVectors.add(secondL.copy());
            firstL.rotate90Clockwise();
            secondL.rotate90Clockwise();
        }
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
