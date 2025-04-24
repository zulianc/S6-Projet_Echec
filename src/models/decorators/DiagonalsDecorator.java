package models.decorators;

import models.Cell;
import models.Game;
import models.pieces.Piece;
import structure.Orientation;
import structure.Position2D;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DiagonalsDecorator extends AccessibleCellsDecorator {
    public DiagonalsDecorator(AccessibleCellsDecorator base) {
        super(base);
        this.possibleVectors = new ArrayList<>();
        this.possibleVectors.add(Orientation.FRONT_LEFT.getVector());
        this.possibleVectors.add(Orientation.FRONT_RIGHT.getVector());
        this.possibleVectors.add(Orientation.BACK_LEFT.getVector());
        this.possibleVectors.add(Orientation.BACK_RIGHT.getVector());
    }

    @Override
    protected List<Cell> getDecoratorAccessibleCells(Game game, Piece piece) {
        List<Cell> accessibleCells = new LinkedList<>();

        for (Position2D vector : this.possibleVectors) {
            Cell nextCell = game.getBoard().getCellOfPiece(piece);
            boolean pieceIsBlocked = false;
            while (!pieceIsBlocked) {
                nextCell = game.getBoard().getCellAtRelativePosition(nextCell, vector);

                if (nextCell == null) {
                    pieceIsBlocked = true;
                } else if (nextCell.hasPiece()) {
                    if (nextCell.getPiece().getTeam() != piece.getTeam()) {
                        accessibleCells.add(nextCell);
                    }
                    pieceIsBlocked = true;
                } else {
                    accessibleCells.add(nextCell);
                }
            }
        }

        return accessibleCells;
    }
}
