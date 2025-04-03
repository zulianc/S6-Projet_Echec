package models;

import models.pieces.*;
import structure.Observable;
import structure.Position;

import java.util.List;

public class Game extends Observable implements Runnable {
    private final ChessBoard chessBoard;
    private final List<Player> players;
    private final int playerCount;
    private int actualPlayer;
    public Move move;

    public Game(List<Player> players) {
        this.players     = players;
        this.playerCount = players.size();
        for (Player player : players) {
            player.setGame(this);
        }
        this.chessBoard = new ChessBoard();
    }

    @Override
    public void run() {
        this.playGame();
    }

    public void playGame() {
        initializePieces();
        while (!(this.gameEnded())) {
            Player p = this.nextPlayer();
            Move m;
            do {
                m = p.getMove();
            } while (!this.validMove(m));
            this.applyMove(m);
            this.updateAll();
            System.out.println("Move played");
        }
    }

    public void sendMove(Move m) {
        this.move = m;
        synchronized (this) {
            notify();
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
        return players.get((this.actualPlayer + 1) % playerCount);
    }

    private boolean validMove(Move m) {
        boolean isValid = false;
        Position sourcePosition = m.source();
        Cell sourceCell = this.chessBoard.getCell(sourcePosition);
        if (sourceCell.hasPiece()) {
            Piece piece = sourceCell.getPiece();
            List<Cell> accessibleCells = piece.getAccessibleCells(chessBoard);
            if (accessibleCells.contains(sourceCell)) {
                isValid = true;
            }
        }

        return isValid;
    }

    private void applyMove(Move m) {
        Cell startCell = this.chessBoard.getCell(m.source().getX(), m.source().getY());
        Piece movedPiece = startCell.getPiece();
        startCell.setPiece(null);
        Cell endCell = this.chessBoard.getCell(m.destination().getX(), m.destination().getY());
        endCell.setPiece(movedPiece);
    }

    public ChessBoard getBoard() {
        return this.chessBoard;
    }
}
