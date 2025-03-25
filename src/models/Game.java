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
                assert p != null;
                m = p.getMove();
            } while (!this.validMove());
            this.applyMove(m);
        }
    }

    private void initializePieces() {

        System.out.println("Initializing pieces...");
        Rook rook1 = new Rook(1);
        System.out.println(rook1);
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
