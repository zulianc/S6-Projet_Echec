package models.decorators.checkers;

import models.boards.Cell;
import models.boards.GameBoard;
import models.boards.GameMove;
import models.boards.PieceMove;
import models.decorators.PossibleMovesDecorator;
import models.games.Game;
import models.pieces.Piece;
import structure.Orientation;
import structure.Position2D;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CheckerPawnDecorator extends PossibleMovesDecorator {
    public CheckerPawnDecorator(PossibleMovesDecorator base) {
        super(base);
        this.possibleVectors = new ArrayList<>();
        this.possibleVectors.add(Orientation.FRONT_RIGHT.getVector());
        this.possibleVectors.add(Orientation.FRONT_LEFT.getVector());
    }

    @Override
    protected List<GameMove> getDecoratorPossibleMoves(Game game, Piece piece) {
        List<GameMove> possibleMoves = new LinkedList<>();

        Cell startingCell = game.getBoard().getCellOfPiece(piece);

        List<Position2D> orientations = new ArrayList<>();
        for (Position2D possibleOrientation : this.possibleVectors) {
            orientations.add(possibleOrientation.rotate(piece.getTeam(), game.getPlayerCount()));
        }

        // TAKING PIECES
        List<Cell> path = new ArrayList<>();
        path.add(startingCell);

        List<Piece> takenPieces = new ArrayList<>();

        List<Position2D> captureOrientations = new ArrayList<>();
        for (Position2D possibleOrientation : this.possibleVectors) {
            captureOrientations.add(possibleOrientation);
            captureOrientations.add(possibleOrientation.rotate180Clockwise());
        }

        possibleMoves.addAll(visitCell(path, takenPieces, captureOrientations, game.getBoard()));

        // NORMAL MOVE
        for (Position2D orientation : orientations) {
            Cell finalCell = game.getBoard().getCellAtRelativePosition(startingCell, orientation);
            if (finalCell != null && !finalCell.hasPiece()) {
                possibleMoves.add(new GameMove(startingCell, finalCell));
            }
        }

        return possibleMoves;
    }

    protected List<GameMove> visitCell(List<Cell> path, List<Piece> takenPieces, List<Position2D> orientations, GameBoard board) {
        List<GameMove> possibleMoves = new LinkedList<>();

        Cell currentCell = path.getLast();

        boolean canContinueMoving = false;

        for (Position2D orientation : orientations) {
            Cell captureCell = board.getCellAtRelativePosition(currentCell, orientation);
            if (captureCell == null) continue;
            Cell destinationCell = board.getCellAtRelativePosition(captureCell, orientation);
            if (destinationCell == null) continue;

            if (this.containsPiecesOfDifferentTeams(captureCell, path.getFirst()) && !takenPieces.contains(captureCell.getPiece()) && !(destinationCell.hasPiece() && !destinationCell.equals(path.getFirst()))) {
                canContinueMoving = true;

                path.addLast(captureCell);
                path.addLast(destinationCell);
                takenPieces.addLast(captureCell.getPiece());
                possibleMoves.addAll(this.visitCell(path, takenPieces, orientations, board));
                takenPieces.removeLast();
                path.removeLast();
                path.removeLast();
            }
        }

        if (!canContinueMoving && path.size() > 1) {
            List<PieceMove> piecePath = new LinkedList<>();
            for (int i = 0; i <= path.size() - 3; i += 2) {
                piecePath.add(new PieceMove(path.get(i), path.get(i + 2), path.get(i + 1)));
            }
            piecePath.add(new PieceMove(path.getLast(), path.getLast()));
            possibleMoves.add(new GameMove(piecePath));
        }

        return possibleMoves;
    }
}
