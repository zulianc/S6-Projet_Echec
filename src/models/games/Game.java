package models.games;

import models.boards.Cell;
import models.boards.GameBoard;
import models.boards.PlayerMove;
import models.pieces.Piece;
import models.players.Player;
import structure.Observable;
import structure.Observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Game extends Observable implements Runnable {
    protected final GameBoard board;
    protected final List<Player> players;
    protected Player actualPlayer;
    protected int turn;
    protected List<String> moves = new ArrayList<>();
    public PlayerMove playerMove;

    public Game(List<Player> players) {
        this.players = players;
        for (Player player : players) {
            if (player instanceof Observer) {
                this.addObserver((Observer) player);
            }
            player.startGame(this);
        }
        this.board = new GameBoard(8);
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
                PlayerMove m;
                do {
                    this.updateAll();
                    m = this.actualPlayer.getMove();
                } while (!this.isValidMove(m, this.actualPlayer));
                this.applyMove(m);
                this.checkIfPlayerLost(this.nextPlayer());
                this.updateAll();

                System.out.println(moves);
            }
        }
        this.lastMove();
        String[] s = new String[]{"gameEnded"};
        updateAllWithParams(s);
        System.out.println("game ended");

        for (Player player : players) {
            synchronized (player) {
                player.notify();
            }
        }
    }

    public void sendMove(PlayerMove m) {
        this.playerMove = m;
        synchronized (this) {
            notify();
        }
    }

    protected Player nextPlayer() {
        return this.players.get((players.indexOf(this.actualPlayer)+1) % this.players.size());
    }

    public abstract List<Cell> getValidCells(Piece piece, Player p);

    protected abstract void initializePieces();

    protected abstract void checkIfPlayerLost(Player p);

    public abstract boolean hasGameEnded();

    protected abstract void lastMove();

    protected abstract boolean isValidMove(PlayerMove m, Player p);

    protected abstract void applyMove(PlayerMove m);

    public GameBoard getBoard() {
        return this.board;
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

    public List<String> getMoves() {
        return this.moves;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public boolean isDraw() {
        int playerCount = 0;
        for (Player player : this.players) {
            if (player.isAlive()) {
                playerCount++;
            }
        }
        return playerCount > 1;
    }
}
