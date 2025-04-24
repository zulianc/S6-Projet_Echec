package models.decorators;

import models.Cell;
import models.Game;
import models.pieces.ChessPawn;
import models.pieces.Piece;
import structure.Orientation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EnPassantDecorator extends AccessibleCellsDecorator {
    public EnPassantDecorator(AccessibleCellsDecorator base) {
        super(base);
        this.possibleOrientations = new ArrayList<>();
        this.possibleOrientations.add(Orientation.FRONT_LEFT);
        this.possibleOrientations.add(Orientation.FRONT_RIGHT);
    }

    @Override
    protected List<Cell> getDecoratorAccessibleCells(Game game, Piece piece) {
        List<Cell> accessibleCells = new LinkedList<>();

        Cell startingCell = game.getBoard().getCellOfPiece(piece);

        for (Orientation orientation : this.possibleOrientations) {
            Orientation pieceOrientation = orientation.copy();
            pieceOrientation.rotate(piece.getTeam(), game.getPlayerCount());

            Orientation capturedPawnOrientation = pieceOrientation.copy();
            capturedPawnOrientation.add(Orientation.BACK);
            capturedPawnOrientation.rotate(piece.getTeam(), game.getPlayerCount());

            Cell cellToMoveAt = game.getBoard().getCellAtRelativePosition(startingCell, pieceOrientation.getVector());
            Cell cellToCapture = game.getBoard().getCellAtRelativePosition(cellToMoveAt, capturedPawnOrientation.getVector());

            if (cellToMoveAt != null && cellToCapture != null && cellToCapture.getPiece() != null) {
                if (this.containsPiecesOfDifferentTeams(startingCell, cellToCapture)) {
                    if (cellToCapture.getPiece() instanceof ChessPawn) {
                        if (cellToCapture.getPiece().getMoveCount() == 1) {
                            if (cellToCapture.getPiece().getLastMoveTurn() > (game.getTurn() - game.getPlayerCount())) {
                                accessibleCells.add(cellToMoveAt);
                            }
                        }
                    }
                }
            }
        }

        return accessibleCells;
    }
}
