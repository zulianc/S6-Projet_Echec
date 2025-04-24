package models.decorators;

import models.Cell;
import models.Game;
import models.pieces.Piece;
import structure.Orientation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChessPawnDecorator extends AccessibleCellsDecorator{
    public ChessPawnDecorator(AccessibleCellsDecorator base) {
        super(base);
        this.possibleOrientations = new ArrayList<>();
        this.possibleOrientations.add(Orientation.FRONT_LEFT);
        this.possibleOrientations.add(Orientation.FRONT_RIGHT);
    }

    @Override
    protected List<Cell> getDecoratorAccessibleCells(Game game, Piece piece) {
        List<Cell> accessibleCells = new LinkedList<>();

        Cell startingCell = game.getBoard().getCellOfPiece(piece);

        // EATING
        for (Orientation orientation : this.possibleOrientations) {
            Orientation pieceOrientation = orientation.copy();
            pieceOrientation.rotate(piece.getTeam(), game.getPlayerCount());

            Cell nextCell = game.getBoard().getCellAtRelativePosition(startingCell, pieceOrientation.getVector());

            if (nextCell != null && containsPiecesOfDifferentTeams(nextCell, startingCell)) {
                accessibleCells.add(nextCell);
            }
        }

        // MOVING
        Orientation pieceOrientation = Orientation.FRONT;
        pieceOrientation.rotate(piece.getTeam(), game.getPlayerCount());

        Cell nextCell = game.getBoard().getCellAtRelativePosition(startingCell, pieceOrientation.getVector());

        if (nextCell != null && !nextCell.hasPiece()) {
            accessibleCells.add(nextCell);
        }

        return accessibleCells;
    }
}
