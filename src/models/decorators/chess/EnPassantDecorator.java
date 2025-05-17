package models.decorators.chess;

import models.boards.Cell;
import models.boards.GameMove;
import models.decorators.PossibleMovesDecorator;
import models.games.Game;
import models.pieces.chess.ChessPawn;
import models.pieces.Piece;
import structure.Orientation;
import structure.Position2D;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EnPassantDecorator extends PossibleMovesDecorator {
    public EnPassantDecorator(PossibleMovesDecorator base) {
        super(base);
        this.possibleVectors = new ArrayList<>();
        this.possibleVectors.add(Orientation.FRONT_LEFT.getVector());
        this.possibleVectors.add(Orientation.FRONT_RIGHT.getVector());
    }

    @Override
    protected List<GameMove> getDecoratorPossibleMoves(Game game, Piece piece) {
        List<GameMove> possibleMoves = new LinkedList<>();

        Cell startingCell = game.getBoard().getCellOfPiece(piece);

        for (Position2D vector : this.possibleVectors) {
            Position2D pieceVector = vector.rotate(piece.getTeam(), game.getPlayerCount());

            Position2D capturedPawnVector = vector.add(Orientation.BACK.getVector()).rotate(piece.getTeam(), game.getPlayerCount());

            Cell cellToMoveAt = game.getBoard().getCellAtRelativePosition(startingCell, pieceVector);
            Cell cellToCapture = game.getBoard().getCellAtRelativePosition(startingCell, capturedPawnVector);

            if (cellToMoveAt != null && cellToCapture != null && cellToCapture.getPiece() != null) {
                if (this.containsPiecesOfDifferentTeams(startingCell, cellToCapture)) {
                    if (cellToCapture.getPiece() instanceof ChessPawn) {
                        if (cellToCapture.getPiece().getMoveCount() == 1) {
                            if (cellToCapture.getPiece().getLastMoveTurn() > (game.getTurn() - game.getPlayerCount())) {
                                possibleMoves.add(new GameMove(startingCell, cellToMoveAt, cellToCapture));
                            }
                        }
                    }
                }
            }
        }

        return possibleMoves;
    }
}
