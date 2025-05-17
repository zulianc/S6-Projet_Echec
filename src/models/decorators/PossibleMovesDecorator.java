package models.decorators;

import models.boards.Cell;
import models.boards.GameMove;
import models.games.Game;
import models.pieces.Piece;
import structure.Position2D;

import java.util.LinkedList;
import java.util.List;

public abstract class PossibleMovesDecorator {
    protected PossibleMovesDecorator base;
    protected List<Position2D> possibleVectors;

    protected PossibleMovesDecorator(PossibleMovesDecorator base) {
        this.base = base;
    }

    protected abstract List<GameMove> getDecoratorPossibleMoves(Game game, Piece piece);

    public List<GameMove> getPossibleMoves(Game game, Piece piece) {
        if (piece == null) {
            throw new RuntimeException("decorator: piece is null");
        }

        List<GameMove> possibleMoves = new LinkedList<>();
        PossibleMovesDecorator baseDecorator = this;
        while (baseDecorator != null) {
            possibleMoves.addAll(baseDecorator.getDecoratorPossibleMoves(game, piece));
            baseDecorator = baseDecorator.base;
        }
        return possibleMoves;
    }

    protected boolean containsPiecesOfDifferentTeams(Cell cell1, Cell cell2) {
        return cell1.hasPiece() && cell2.hasPiece() && cell1.getPiece().getTeam() != cell2.getPiece().getTeam();
    }

    protected boolean containsPiecesOfSameTeams(Cell cell1, Cell cell2) {
        return cell1.hasPiece() && cell2.hasPiece() && cell1.getPiece().getTeam() == cell2.getPiece().getTeam();
    }
}
