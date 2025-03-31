package models;

import models.pieces.*;

import java.util.List;

public class Game {
    private final ChessBoard chessBoard;
    private List<Player> players;
    private int playerNumber;

    public Game(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    public void playGame() {
        initializePieces();
        while (!(this.gameEnded())) {
            Player p = this.nextPlayer();
            Move m;
            do {
                assert p != null;
                m = p.getMove();
            } while (!this.validMove());
            this.applyMove(m);
        }
    }

    private void initializePieces() {

        System.out.println("Initializing pieces...");
        this.chessBoard.placePieces(new Rook(1), 0, 0);
        this.chessBoard.placePieces(new Rook(1), 7, 0);
        this.chessBoard.placePieces(new Rook(0), 0, 7);
        this.chessBoard.placePieces(new Rook(0), 7, 7);

        this.chessBoard.placePieces(new Knight(1), 1, 0);
        this.chessBoard.placePieces(new Knight(1), 6, 0);
        this.chessBoard.placePieces(new Knight(0), 1, 7);
        this.chessBoard.placePieces(new Knight(0), 6, 7);

        this.chessBoard.placePieces(new Bishop(1), 2, 0);
        this.chessBoard.placePieces(new Bishop(1), 5, 0);
        this.chessBoard.placePieces(new Bishop(0), 2, 7);
        this.chessBoard.placePieces(new Bishop(0), 5, 7);

        this.chessBoard.placePieces(new Queen(1), 3, 0);
        this.chessBoard.placePieces(new Queen(0), 3, 7);

        this.chessBoard.placePieces(new King(1), 4, 0);
        this.chessBoard.placePieces(new King(0), 4, 7);

        for (int x = 0; x < 8; x++) {
            this.chessBoard.placePieces(new ChessPawn(1), x, 1);
        }
        for (int x = 0; x < 8; x++) {
            this.chessBoard.placePieces(new ChessPawn(0), x, 6);
        }
    }

    private boolean gameEnded() {
        return false;
    }

    private Player nextPlayer() {
        return null;
    }

    private boolean validMove() {
        return false;
    }

    private void applyMove(Move m) {
    }
}
