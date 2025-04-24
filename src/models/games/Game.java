package models.games;

import models.boards.Cell;
import models.boards.GameBoard;
import models.boards.Move;
import models.pieces.Piece;
import models.players.Player;
import structure.Observable;
import structure.Observer;

import java.util.List;

public abstract class Game extends Observable implements Runnable {
    protected final GameBoard chessBoard;
    protected final List<Player> players;
    protected Player actualPlayer;
    protected int turn;
    protected Piece promotionPiece;
    public Move playerMove;

    public Game(List<Player> players) {
        this.players = players;
        for (Player player : players) {
            if (player instanceof Observer) {
                this.addObserver((Observer) player);
            }
            player.startGame(this);
        }
        this.chessBoard = new GameBoard(8);
        this.turn = 0;
    }

    @Override
    public void run() {
        this.playGame();
    }

    public void playGame() {
        this.initializePieces();
        while (!this.hasGameEnded()) {
            this.turn++;
            this.actualPlayer = this.nextPlayer();
            if (this.actualPlayer.isAlive()) {
                Move m;
                do {
                    this.updateAll();
                    m = this.actualPlayer.getMove();
                } while (!this.isValidMove(m, this.actualPlayer));
                this.applyMove(m);
                this.checkIfPlayerLost(this.nextPlayer());
                this.updateAll();
            }
        }
        String[] s = new String[]{"gameEnded"};
        updateAllWithParams(s);
    }

    public void sendMove(Move m) {
        this.playerMove = m;
        synchronized (this) {
            notify();
        }
    }

    private Player nextPlayer() {
        return this.players.get((players.indexOf(this.actualPlayer)+1) % this.players.size());
    }

    protected abstract void initializePieces();

    protected abstract void checkIfPlayerLost(Player p);

    public abstract boolean hasGameEnded();

    public abstract List<Cell> getValidCells(Piece piece, Player p);

    protected abstract boolean isValidMove(Move m, Player p);

    protected abstract void applyMove(Move m);

    public GameBoard getBoard() {
        return this.chessBoard;
    }

    public int getPlayerCount() {
        return this.players.size();
    }

    public Player getActualPlayer() {
        return this.actualPlayer;
    }

    public int getTurn() {
        return this.turn;
    }
}
