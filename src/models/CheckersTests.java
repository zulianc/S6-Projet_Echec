package models;

import models.boards.GameBoard;
import models.pieces.Piece;
import models.pieces.checkers.CheckerPawn;
import models.pieces.checkers.CheckerQueen;

public abstract class CheckersTests {
    private static void emptyBoard(GameBoard board) {
        for (Piece piece : board.getAllPieces()) {
            board.removePieceFromBoard(piece);
        }
    }

    public static void testPromotion(GameBoard board) {
        emptyBoard(board);

        board.setPieceToCell(new CheckerPawn(0), 2, 1);
        board.setPieceToCell(new CheckerPawn(0), 3, 2);

        board.setPieceToCell(new CheckerPawn(1), 4, 1);
        board.setPieceToCell(new CheckerPawn(1), 6, 1);

        board.setPieceToCell(new CheckerPawn(1), 9, 6);
    }

    public static void testChoices(GameBoard board) {
        emptyBoard(board);

        board.setPieceToCell(new CheckerPawn(0), 5, 8);

        board.setPieceToCell(new CheckerPawn(1), 6, 7);

        board.setPieceToCell(new CheckerPawn(1), 4, 7);
        board.setPieceToCell(new CheckerPawn(1), 2, 5);
        board.setPieceToCell(new CheckerPawn(1), 2, 3);

        board.setPieceToCell(new CheckerPawn(1), 4, 5);
        board.setPieceToCell(new CheckerPawn(1), 6, 3);
    }

    public static void testLoops(GameBoard board) {
        emptyBoard(board);

        board.setPieceToCell(new CheckerPawn(0), 4, 5);

        board.setPieceToCell(new CheckerPawn(1), 3, 4);
        board.setPieceToCell(new CheckerPawn(1), 5, 4);
        board.setPieceToCell(new CheckerPawn(1), 3, 2);
        board.setPieceToCell(new CheckerPawn(1), 5, 2);

        board.setPieceToCell(new CheckerPawn(1), 3, 8);
        board.setPieceToCell(new CheckerPawn(1), 5, 8);
        board.setPieceToCell(new CheckerPawn(1), 3, 6);
        board.setPieceToCell(new CheckerPawn(1), 5, 6);
    }

    public static void testQueen(GameBoard board) {
        emptyBoard(board);

        board.setPieceToCell(new CheckerQueen(0), 9, 6);

        board.setPieceToCell(new CheckerPawn(1), 8, 5);
        board.setPieceToCell(new CheckerPawn(1), 6, 5);
        board.setPieceToCell(new CheckerPawn(1), 5, 4);
        board.setPieceToCell(new CheckerPawn(1), 6, 7);
        board.setPieceToCell(new CheckerPawn(1), 4, 7);
    }
}
