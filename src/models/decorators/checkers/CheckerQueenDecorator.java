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

public class CheckerQueenDecorator extends PossibleMovesDecorator {
    public CheckerQueenDecorator(PossibleMovesDecorator base) {
        super(base);
        this.possibleVectors = new ArrayList<>();
        this.possibleVectors.add(Orientation.FRONT_RIGHT.getVector());
        this.possibleVectors.add(Orientation.FRONT_LEFT.getVector());
        this.possibleVectors.add(Orientation.BACK_RIGHT.getVector());
        this.possibleVectors.add(Orientation.BACK_LEFT.getVector());
    }

    @Override
    protected List<GameMove> getDecoratorPossibleMoves(Game game, Piece piece) {
        List<GameMove> possibleMoves = new LinkedList<>();

        Cell startingCell = game.getBoard().getCellOfPiece(piece);

        // TAKING PIECES
        List<Cell> path = new ArrayList<>();
        path.add(startingCell);

        List<Piece> takenPieces = new ArrayList<>();

        possibleMoves.addAll(visitCell(path, takenPieces, game.getBoard()));

        // NORMAL MOVE
        for (Position2D orientation : this.possibleVectors) {
            Cell finalCell = startingCell;
            boolean pieceIsBlocked = false;

            while (!pieceIsBlocked) {
                finalCell = game.getBoard().getCellAtRelativePosition(finalCell, orientation);

                if (finalCell == null || finalCell.hasPiece()) {
                    pieceIsBlocked = true;
                }
                else {
                    possibleMoves.add(new GameMove(startingCell, finalCell));
                }
            }
        }

        return possibleMoves;
    }

    protected List<GameMove> visitCell(List<Cell> path, List<Piece> takenPieces, GameBoard board) {
        List<GameMove> possibleMoves = new LinkedList<>();

        Cell currentCell = path.getLast();

        boolean canContinueMoving = false;

        for (Position2D orientation : this.possibleVectors) {
            Cell captureCell = currentCell;
            Cell destinationCell = board.getCellAtRelativePosition(currentCell, orientation);
            if (destinationCell == null) {
                continue;
            }

            boolean pieceIsBlocked = false;
            boolean hasCaptured = false;
            while (!pieceIsBlocked) {
                if (!hasCaptured) {
                    captureCell = board.getCellAtRelativePosition(captureCell, orientation);
                }
                destinationCell = board.getCellAtRelativePosition(destinationCell, orientation);

                if (destinationCell == null) {
                    pieceIsBlocked = true;
                }
                else {
                    if (hasCaptured && destinationCell.hasPiece()) {
                        pieceIsBlocked = true;
                    }
                    else if (this.containsPiecesOfSameTeams(path.getFirst(), captureCell) || (this.containsPiecesOfSameTeams(path.getFirst(), destinationCell) && !destinationCell.equals(path.getFirst()))) {
                        pieceIsBlocked = true;
                    }
                    else {
                        if (this.containsPiecesOfDifferentTeams(path.getFirst(), captureCell) && !takenPieces.contains(captureCell.getPiece()) && !(destinationCell.hasPiece() && !destinationCell.equals(path.getFirst()))) {
                            canContinueMoving = true;
                            hasCaptured = true;

                            path.addLast(captureCell);
                            path.addLast(destinationCell);
                            takenPieces.addLast(captureCell.getPiece());
                            possibleMoves.addAll(this.visitCell(path, takenPieces, board));
                            takenPieces.removeLast();
                            path.removeLast();
                            path.removeLast();
                        }
                    }
                }
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
