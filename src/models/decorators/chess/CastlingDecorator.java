package models.decorators.chess;

import models.boards.Cell;
import models.boards.GameMove;
import models.boards.PieceMove;
import models.decorators.PossibleMovesDecorator;
import models.games.ChessGame;
import models.games.Game;
import models.boards.PlayerMove;
import models.pieces.Piece;
import models.pieces.chess.King;
import models.pieces.chess.Rook;
import structure.Orientation;
import structure.Position2D;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CastlingDecorator extends PossibleMovesDecorator {
    public CastlingDecorator (PossibleMovesDecorator base) {
        super(base);
        this.possibleVectors = new ArrayList<>();
        this.possibleVectors.add(Orientation.LEFT.getVector());
        this.possibleVectors.add(Orientation.RIGHT.getVector());
    }

    @Override
    protected List<GameMove> getDecoratorPossibleMoves(Game game, Piece piece) {
        /*
        Castling rules for both normal chess and 960 chess:
        - the king and rook must have never moved
        - the rook and king's final positions are the same in both variants
        - the king always starts with one rook to its left and one rook to its right in both variants
        - every cell from the king's starting position to its end position must not be under attack
        - every cell from the king and the rook's starting positions to their end positions must be empty
         */

        List<GameMove> possibleMoves = new LinkedList<>();

        Cell startingCell = game.getBoard().getCellOfPiece(piece);

        if (piece.hasNeverMoved()) {
            // Check team to prevent infinite loop when checking for checks, works because castling is not a capturing move
            if (piece.getTeam() == game.getActualPlayer().getTeam()) {
                for (Position2D vector : this.possibleVectors) {
                    boolean longCastling = (vector.equals(Orientation.LEFT.getVector()));

                    Position2D pieceVector = vector;
                    // The kings' files are the same in 2 players chess but not in 4 players
                    if (game.getPlayerCount() == 4) {
                        pieceVector = pieceVector.rotate(piece.getTeam(), game.getPlayerCount());
                    }

                    // STEP 1: find the final position of the king and rook
                    Cell finalKingCell;
                    Cell finalRookCell;
                    {
                        Cell nextCell = startingCell;
                        boolean endOfBoard = false;
                        do {
                            Cell followingCell = game.getBoard().getCellAtRelativePosition(nextCell, vector);
                            if (followingCell == null) {
                                endOfBoard = true;
                            }
                            else {
                                nextCell = followingCell;
                            }
                        } while (!endOfBoard);

                        Position2D inversedVector = pieceVector.rotate180Clockwise();
                        Position2D boardEndToKingVector = inversedVector;
                        if (longCastling) {
                            boardEndToKingVector = boardEndToKingVector.add(inversedVector);
                        }

                        finalKingCell = game.getBoard().getCellAtRelativePosition(nextCell, boardEndToKingVector);

                        boardEndToKingVector = boardEndToKingVector.add(inversedVector);

                        finalRookCell = game.getBoard().getCellAtRelativePosition(nextCell, boardEndToKingVector);
                    }

                    // STEP 2: check for the rook's existence and that the path is clear
                    Cell startingRookCell = null;
                    {
                        Cell nextCell = startingCell;
                        boolean reachedEndOfBoard = false;
                        boolean foundValidRook = false;
                        boolean reachedFinalKingCell = false;
                        boolean blockedPath = false;

                        boolean inversedKingMovement = false;
                        int kingDirection = game.getBoard().getDistanceFromMove(new PlayerMove(startingCell, finalKingCell)).getX();
                        if (kingDirection != 0) {
                            kingDirection = kingDirection / (Math.abs(kingDirection));
                            inversedKingMovement = (kingDirection != vector.getX());
                        }

                        if (inversedKingMovement) {
                            nextCell = finalKingCell;

                            do {
                                if (nextCell == null || ((ChessGame) game).isInCheckIfMove(new PlayerMove(startingCell, nextCell))) {
                                    blockedPath = true;
                                }

                                nextCell = game.getBoard().getCellAtRelativePosition(nextCell, vector);
                            } while (!nextCell.equals(startingCell) && !blockedPath);

                            nextCell = startingCell;
                            reachedFinalKingCell = true;
                        }

                        do {
                            if (nextCell == null) {
                                reachedEndOfBoard = true;
                            } else {
                                if (this.containsPiecesOfSameTeams(nextCell, startingCell)
                                        && nextCell.getPiece() instanceof Rook
                                        && nextCell.getPiece().hasNeverMoved()) {
                                    foundValidRook = true;
                                    startingRookCell = nextCell;
                                }
                                else {
                                    if (nextCell.hasPiece() && !nextCell.equals(startingCell)) {
                                        blockedPath = true;
                                    } else {
                                        if (!reachedFinalKingCell) {
                                            if (((ChessGame) game).isInCheckIfMove(new PlayerMove(startingCell, nextCell))) {
                                                blockedPath = true;
                                            }
                                        }
                                    }
                                }

                                if (nextCell.equals(finalKingCell)) {
                                    reachedFinalKingCell = true;
                                }
                            }

                            if (!reachedEndOfBoard) {
                                nextCell = game.getBoard().getCellAtRelativePosition(nextCell, vector);
                            }
                        } while (!(foundValidRook && reachedFinalKingCell) && !reachedEndOfBoard && !blockedPath);

                        if (!reachedEndOfBoard && !blockedPath && foundValidRook && !(finalRookCell.hasPiece() && !(finalRookCell.getPiece() instanceof King))) {
                            Cell safeCell = null;
                            for (Cell[] cells : game.getBoard().getCells()) {
                                for (Cell cell : cells) {
                                    if (!cell.hasPiece()) {
                                        safeCell = cell;
                                    }
                                }
                            }

                            boolean altMove = (finalKingCell.equals(startingRookCell));

                            List<PieceMove> moves = new LinkedList<>();
                            moves.addLast(new PieceMove(startingCell, finalKingCell));
                            if (altMove) {
                                moves.addLast(new PieceMove(startingCell, safeCell));
                            }
                            moves.addLast(new PieceMove(startingRookCell, finalRookCell));
                            if (altMove) {
                                moves.addLast(new PieceMove(safeCell, finalKingCell));
                            }
                            possibleMoves.add(new GameMove(moves));

                            if (Math.abs(game.getBoard().getDistanceFromMove(new PlayerMove(startingCell, finalKingCell)).getX()) == 1 && !finalKingCell.hasPiece()) {
                                List<PieceMove> dullMoves = new LinkedList<>();
                                dullMoves.addLast(new PieceMove(startingCell, finalKingCell));
                                dullMoves.addLast(new PieceMove(startingRookCell, startingRookCell));
                                possibleMoves.add(new GameMove(dullMoves));
                            }
                        }
                    }
                }
            }
        }

        return possibleMoves;
    }
}
