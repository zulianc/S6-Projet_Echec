package models;

import models.pieces.Move;
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
        while (!(this.gameEnded())) {
            Player p = this.nextPlayer();
            Move m;
            do {
                m = p.getMove();
            } while (!this.validMove());
            this.applyMove(m);
        }
    }

    private void initializePieces() {
        Rook rook1 = new Rook(0);
        Rook rook2 = new Rook(0);
        Rook rook3 = new Rook(1);
        Rook rook4 = new Rook(1);

        this.chessBoard.placePieces(rook1, 0, 0);
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
