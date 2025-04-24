package models.decorators;

import models.Cell;
import models.Game;
import models.Move;
import models.pieces.Piece;
import models.pieces.Rook;
import structure.Orientation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CastlingDecorator extends AccessibleCellsDecorator {
    public CastlingDecorator (AccessibleCellsDecorator base) {
        super(base);
        this.possibleOrientations = new ArrayList<>();
        this.possibleOrientations.add(Orientation.LEFT);
        this.possibleOrientations.add(Orientation.RIGHT);
    }

    @Override
    protected List<Cell> getDecoratorAccessibleCells(Game game, Piece piece) {
        /*
        Castling rules for both normal chess and 960 chess:
        - the king and rook must have never moved
        - the rook and king's final positions are the same in both variants
        - the king always starts with one rook to its left and one rook to its right in both variants
        - every cell from the king's starting position to its end position must not be under attack
        - every cell from the king and the rook's starting positions to their end positions must be empty
         */

        List<Cell> accessibleCells = new LinkedList<>();

        Cell startingCell = game.getBoard().getCellOfPiece(piece);

        if (piece.hasNeverMoved()) {
            // Check team to prevent infinite loop when checking for checks, works because castling is not a capturing move
            if (piece.getTeam() == game.getActualPlayer().getTeam()) {
                for (Orientation orientation : this.possibleOrientations) {
                    boolean longCastling = (orientation == Orientation.LEFT);

                    Orientation pieceOrientation = orientation.copy();
                    // The kings' files are the same in 2 players chess but not in 4 players
                    if (game.getPlayerCount() == 4) {
                        pieceOrientation.rotate(piece.getTeam(), game.getPlayerCount());
                    }

                    // STEP 1: find the final position of the king
                    Cell finalKingCell;
                    Cell finalRookCell;
                    {
                        Cell nextCell = startingCell;
                        boolean endOfBoard = false;
                        do {
                            Cell followingCell = game.getBoard().getCellAtRelativePosition(nextCell, orientation.getVector());
                            if (followingCell == null) {
                                endOfBoard = true;
                            }
                            else {
                                nextCell = followingCell;
                            }
                        } while (!endOfBoard);

                        Orientation inversedOrientation = pieceOrientation.copy();
                        inversedOrientation.rotate180Clockwise();
                        Orientation kingOrientation = inversedOrientation.copy();
                        if (longCastling) {
                            kingOrientation.add(inversedOrientation);
                        }

                        finalKingCell = game.getBoard().getCellAtRelativePosition(nextCell, kingOrientation.getVector());

                        kingOrientation.add(inversedOrientation);

                        finalRookCell = game.getBoard().getCellAtRelativePosition(nextCell, kingOrientation.getVector());
                    }

                    // STEP 2: check for the rook's existence and that the path is clear
                    {
                        Cell nextCell = startingCell;
                        boolean endOfBoard = false;
                        boolean foundValidRook = false;
                        boolean reachedFinalKingCell = false;
                        boolean blockedPath = false;
                        do {
                            if (nextCell == null) {
                                endOfBoard = true;
                            } else {
                                if (this.containsPiecesOfSameTeams(nextCell, startingCell)
                                        && nextCell.getPiece() instanceof Rook
                                        && nextCell.getPiece().hasNeverMoved()) {
                                    foundValidRook = true;
                                }
                                else {
                                    if (nextCell.hasPiece()) {
                                        blockedPath = true;
                                    } else {
                                        if (!reachedFinalKingCell) {
                                            if (!game.isntInCheckIfMove(new Move(startingCell, nextCell))) {
                                                blockedPath = true;
                                            }
                                        }
                                    }
                                }

                                if (nextCell.equals(finalKingCell)) {
                                    reachedFinalKingCell = true;
                                }
                            }

                            if (!endOfBoard) {
                                nextCell = game.getBoard().getCellAtRelativePosition(nextCell, orientation.getVector());
                            }
                        } while (!(foundValidRook && reachedFinalKingCell) && !endOfBoard && !blockedPath);

                        if (endOfBoard || blockedPath) {
                            continue;
                        }
                    }

                    accessibleCells.add(finalKingCell);
                }
            }
        }

        return accessibleCells;
    }
}
