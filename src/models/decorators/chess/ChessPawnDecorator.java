package models.decorators.chess;

import models.boards.Cell;
import models.boards.GameMove;
import models.decorators.PossibleMovesDecorator;
import models.games.Game;
import models.pieces.Piece;
import structure.Orientation;
import structure.Position2D;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChessPawnDecorator extends PossibleMovesDecorator {
    public ChessPawnDecorator(PossibleMovesDecorator base) {
        super(base);
        this.possibleVectors = new ArrayList<>();
        this.possibleVectors.add(Orientation.FRONT_LEFT.getVector());
        this.possibleVectors.add(Orientation.FRONT_RIGHT.getVector());
    }

    @Override
    protected List<GameMove> getDecoratorPossibleMoves(Game game, Piece piece) {
        List<GameMove> possibleMoves = new LinkedList<>();

        Cell startingCell = game.getBoard().getCellOfPiece(piece);

        // EATING
        for (Position2D vector : this.possibleVectors) {
            Position2D pieceVector = vector.rotate(piece.getTeam(), game.getPlayerCount());

            Cell nextCell = game.getBoard().getCellAtRelativePosition(startingCell, pieceVector);

            if (nextCell != null && containsPiecesOfDifferentTeams(nextCell, startingCell)) {
                possibleMoves.add(new GameMove(startingCell, nextCell));
            }
        }

        // MOVING
        Position2D pieceVector = Orientation.FRONT.getVector().rotate(piece.getTeam(), game.getPlayerCount());

        Cell nextCell = game.getBoard().getCellAtRelativePosition(startingCell, pieceVector);

        if (nextCell != null && !nextCell.hasPiece()) {
            possibleMoves.add(new GameMove(startingCell, nextCell));
        }

        return possibleMoves;
    }
}
