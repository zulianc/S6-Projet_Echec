package models;

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

    private void initializePieces() {}
}
