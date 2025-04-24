package models.decorators;

import models.Cell;
import models.Game;
import models.pieces.ChessPawn;
import models.pieces.Piece;
import structure.Orientation;
import structure.Position2D;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EnPassantDecorator extends AccessibleCellsDecorator {
    public EnPassantDecorator(AccessibleCellsDecorator base) {
        super(base);
        this.possibleVectors = new ArrayList<>();
        this.possibleVectors.add(Orientation.FRONT_LEFT.getVector());
        this.possibleVectors.add(Orientation.FRONT_RIGHT.getVector());
    }

    @Override
    protected List<Cell> getDecoratorAccessibleCells(Game game, Piece piece) {
        List<Cell> accessibleCells = new LinkedList<>();

        Cell startingCell = game.getBoard().getCellOfPiece(piece);

        for (Position2D vector : this.possibleVectors) {
            Position2D pieceVector = vector.copy();
            pieceVector.rotate(piece.getTeam(), game.getPlayerCount());

            Position2D capturedPawnVector = vector.copy();
            capturedPawnVector.add(Orientation.BACK.getVector());
            capturedPawnVector.rotate(piece.getTeam(), game.getPlayerCount());

            Cell cellToMoveAt = game.getBoard().getCellAtRelativePosition(startingCell, pieceVector);
            Cell cellToCapture = game.getBoard().getCellAtRelativePosition(startingCell, capturedPawnVector);

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
