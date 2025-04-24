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

public class FirstMoveJumpDecorator extends AccessibleCellsDecorator {
    public FirstMoveJumpDecorator (AccessibleCellsDecorator base) {
        super(base);
        this.possibleVectors = new ArrayList<>();
        this.possibleVectors.add(Orientation.FRONT.getVector());
    }

    @Override
    protected List<Cell> getDecoratorAccessibleCells(Game game, Piece piece) {
        List<Cell> accessibleCells = new LinkedList<>();

        Cell startingCell = game.getBoard().getCellOfPiece(piece);

        if (startingCell.hasPiece() && startingCell.getPiece().hasNeverMoved()) {
            for (Position2D vector : this.possibleVectors) {
                Position2D pieceVector = vector.copy();
                pieceVector.rotate(startingCell.getPiece().getTeam(), game.getPlayerCount());

                Cell nextCell = startingCell;
                boolean cellCanMove = true;
                for (int i = 0; i < 2; i++) {
                    nextCell = game.getBoard().getCellAtRelativePosition(nextCell, pieceVector);

                    if (nextCell == null || nextCell.hasPiece()) {
                        cellCanMove = false;
                    }
                }

                if (cellCanMove) {
                    accessibleCells.add(nextCell);
                }
            }
        }

        return accessibleCells;
    }
}
