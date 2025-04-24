package models.decorators;

import models.Cell;
import models.ChessBoard;
import models.Game;
import models.pieces.Piece;
import structure.Orientation;
import structure.Position;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FirstMoveJumpDecorator extends AccessibleCellsDecorator {
    public FirstMoveJumpDecorator (AccessibleCellsDecorator base) {
        super(base);
        this.possibleOrientations = new ArrayList<>();
        this.possibleOrientations.add(Orientation.FRONT);
    }

    @Override
    protected List<Cell> getDecoratorAccessibleCells(Game game, Piece piece) {
        List<Cell> accessibleCells = new LinkedList<>();

        Cell startingCell = game.getBoard().getCellOfPiece(piece);

        if (startingCell.hasPiece() && startingCell.getPiece().hasNeverMoved()) {
            for (Orientation orientation : this.possibleOrientations) {
                Orientation pieceOrientation = orientation;
                pieceOrientation.rotate(startingCell.getPiece().getTeam(), game.getPlayerCount());

                Cell nextCell = startingCell;
                boolean cellCanMove = true;
                for (int i = 0; i < 2; i++) {
                    nextCell = game.getBoard().getCellAtRelativePosition(nextCell, pieceOrientation.getVector());

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
