package models;

import models.pieces.Rook;

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
    }

    private void initializePieces() {
        Rook rook1 = new Rook(0);
        Rook rook2 = new Rook(0);
        Rook rook3 = new Rook(1);
        Rook rook4 = new Rook(1);

        this.chessBoard.placePieces(rook1, 0, 0);
    }
}
