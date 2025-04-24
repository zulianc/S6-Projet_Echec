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

public class ChessPawnDecorator extends AccessibleCellsDecorator {
    public ChessPawnDecorator(AccessibleCellsDecorator base) {
        super(base);
        this.possibleVectors = new ArrayList<>();
        this.possibleVectors.add(Orientation.FRONT_LEFT.getVector());
        this.possibleVectors.add(Orientation.FRONT_RIGHT.getVector());
    }

    @Override
    protected List<Cell> getDecoratorAccessibleCells(Game game, Piece piece) {
        List<Cell> accessibleCells = new LinkedList<>();

        Cell startingCell = game.getBoard().getCellOfPiece(piece);

        // EATING
        for (Position2D vector : this.possibleVectors) {
            Position2D pieceVector = vector.copy();
            pieceVector.rotate(piece.getTeam(), game.getPlayerCount());

            Cell nextCell = game.getBoard().getCellAtRelativePosition(startingCell, pieceVector);

            if (nextCell != null && containsPiecesOfDifferentTeams(nextCell, startingCell)) {
                accessibleCells.add(nextCell);
            }
        }

        // MOVING
        Position2D pieceVector = Orientation.FRONT.getVector();
        pieceVector.rotate(piece.getTeam(), game.getPlayerCount());

        Cell nextCell = game.getBoard().getCellAtRelativePosition(startingCell, pieceVector);

        if (nextCell != null && !nextCell.hasPiece()) {
            accessibleCells.add(nextCell);
        }

        return accessibleCells;
    }
}
