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

public class KingDecorator extends PossibleMovesDecorator {
    public KingDecorator(PossibleMovesDecorator base) {
        super(base);
        this.possibleVectors = new ArrayList<>();
        this.possibleVectors.add(Orientation.FRONT.getVector());
        this.possibleVectors.add(Orientation.FRONT_LEFT.getVector());
        this.possibleVectors.add(Orientation.FRONT_RIGHT.getVector());
        this.possibleVectors.add(Orientation.BACK.getVector());
        this.possibleVectors.add(Orientation.BACK_LEFT.getVector());
        this.possibleVectors.add(Orientation.BACK_RIGHT.getVector());
        this.possibleVectors.add(Orientation.LEFT.getVector());
        this.possibleVectors.add(Orientation.RIGHT.getVector());
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
