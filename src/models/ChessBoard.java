package models;

import models.pieces.Piece;
import structure.Observable;

public class ChessBoard extends Observable {
    public static final int CHESS_BOARD_SIZE = 8;
    private final Cell[][] cells = new Cell[CHESS_BOARD_SIZE][CHESS_BOARD_SIZE];

    public ChessBoard() {
        initializeEmptyBoard();
    }

    private void initializeEmptyBoard() {
        int baseColor;
        for (int y = 0; y < ChessBoard.CHESS_BOARD_SIZE; y++) {
            for (int x = 0; x < ChessBoard.CHESS_BOARD_SIZE; x++) {
                baseColor = ((x+y) % 2 == 0) ? 0 : 1 ;
                this.cells[x][y] = new Cell(baseColor);
            }
        }
    }

    public void placePieces(Piece piece, int x, int y) {
        System.out.println("Placing piece " + piece + " at (" + x + ", " + y + ")");
        this.cells[x][y].setPiece(piece);
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }
}
