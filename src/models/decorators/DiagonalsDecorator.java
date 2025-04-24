package models.decorators;

import models.Cell;
import models.Game;
import models.pieces.Piece;
import structure.Orientation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DiagonalsDecorator extends AccessibleCellsDecorator {
    public DiagonalsDecorator(AccessibleCellsDecorator base) {
        super(base);
        this.possibleOrientations = new ArrayList<>();
        this.possibleOrientations.add(Orientation.FRONT_LEFT);
        this.possibleOrientations.add(Orientation.FRONT_RIGHT);
        this.possibleOrientations.add(Orientation.BACK_LEFT);
        this.possibleOrientations.add(Orientation.BACK_RIGHT);
    }

    @Override
    protected List<Cell> getDecoratorAccessibleCells(Game game, Piece piece) {
        List<Cell> accessibleCells = new LinkedList<>();

        for (Orientation orientation : this.possibleOrientations) {
            Cell nextCell = game.getBoard().getCellOfPiece(piece);
            boolean pieceIsBlocked = false;
            while (!pieceIsBlocked) {
                nextCell = game.getBoard().getCellAtRelativePosition(nextCell, orientation.getVector());

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
