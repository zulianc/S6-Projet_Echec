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

public class KnightDecorator extends PossibleMovesDecorator {
    public KnightDecorator(PossibleMovesDecorator base) {
        super(base);

        this.possibleVectors = new ArrayList<>();

        Position2D firstL = Orientation.LONG_FRONT.getVector().add(Orientation.LEFT.getVector());
        Position2D secondL = Orientation.LONG_FRONT.getVector().add(Orientation.RIGHT.getVector());

        for (int i = 0; i < 4; i++) {
            this.possibleVectors.add(firstL);
            this.possibleVectors.add(secondL);
            firstL = firstL.rotate90Clockwise();
            secondL = secondL.rotate90Clockwise();
        }
    }

    @Override
    protected List<GameMove> getDecoratorPossibleMoves(Game game, Piece piece) {
        List<GameMove> possibleMoves = new LinkedList<>();

        Cell startingCell = game.getBoard().getCellOfPiece(piece);

        for (Position2D vector : this.possibleVectors) {
            Cell nextCell = game.getBoard().getCellAtRelativePosition(startingCell, vector);

            if (nextCell != null && !this.containsPiecesOfSameTeams(nextCell, startingCell)) {
                possibleMoves.add(new GameMove(startingCell, nextCell));
            }
        }

        return possibleMoves;
    }
}
