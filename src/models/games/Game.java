package models.games;

import models.boards.*;
import models.pieces.Piece;
import models.players.Player;
import structure.Observable;
import structure.Observer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Game extends Observable implements Runnable {
    protected final GameBoard board;
    protected final List<Player> players;
    protected Player actualPlayer;
    protected int turn;
    protected List<GameMove> possibleMoves;
    protected List<String> movesNotation = new ArrayList<>();
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
        System.out.println("Initializing pieces...");
        this.initializePieces();
        while (!this.hasGameEnded()) {
            this.turn++;
            this.actualPlayer = this.nextPlayer();
            if (this.actualPlayer.isAlive()) {
                this.updatePossibleMoves();
                PlayerMove playerMove;
                do {
                    do {
                        this.updateAll();
                        playerMove = this.actualPlayer.getMove();
                    } while (!this.canPlayerMove(playerMove));
                    this.applyMove(playerMove);
                } while (!this.possibleMoves.isEmpty());
                this.checkIfPlayerLost(this.nextPlayer());
                this.updateAll();

                System.out.println(movesNotation);
            }
        }
        this.lastMoveNotation();
        String[] s = new String[]{"gameEnded"};
        updateAllWithParams(s);
        System.out.println("Game ended!");

        for (Player player : players) {
            synchronized (player) {
                player.notify();
            }
        }
    }

    public void sendMove(PlayerMove playerMove) {
        this.playerMove = playerMove;
        synchronized (this) {
            notify();
        }
    }

    protected abstract void initializePieces();

    protected abstract void checkIfPlayerLost(Player p);

    public abstract boolean hasGameEnded();

    protected abstract void lastMoveNotation();

    protected abstract void applyMove(PlayerMove m);

    protected abstract boolean isValidMove(GameMove move);

    protected Player nextPlayer() {
        return this.players.get((players.indexOf(this.actualPlayer)+1) % this.players.size());
    }

    protected void updatePossibleMoves() {
        this.possibleMoves = new ArrayList<>();
        for (Piece piece : this.board.getAllPiecesOfTeam(this.actualPlayer.getTeam())) {
            for (GameMove move : piece.getPossibleMoves(this)) {
                if (this.isValidMove(move)) {
                    this.possibleMoves.add(move);
                }
            }
        }
    }

    protected boolean canPlayerMove(PlayerMove playerMove) {
        for (GameMove move : this.possibleMoves) {
            if (move.moves().getFirst().source().equals(playerMove.source()) && move.moves().getFirst().destination().equals(playerMove.destination())) {
                return true;
            }
        }
        return false;
    }

    protected Piece doMove(PieceMove move) {
        Piece movedPiece = move.source().getPiece();
        Piece deadPiece = move.captured().getPiece();

        this.board.removePieceFromCell(deadPiece);
        this.board.setPieceToCell(null, move.source());
        this.board.setPieceToCell(movedPiece, move.destination());

        movedPiece.signalPieceJustMoved(this.turn);

        return deadPiece;
    }

    protected List<Piece> doMove(GameMove move) {
        List<Piece> deadPieces = new LinkedList<>();

        for (PieceMove pieceMove : move.moves()) {
            Piece deadPiece = this.doMove(pieceMove);
            deadPieces.addLast(deadPiece);
        }

        return deadPieces;
    }

    protected void undoMove(PieceMove move, Piece deadPiece) {
        Piece movedPiece = move.destination().getPiece();

        this.board.removePieceFromCell(movedPiece);
        this.board.setPieceToCell(movedPiece, move.source());
        this.board.setPieceToCell(deadPiece, move.captured());

        movedPiece.signalPieceUnmoved();
    }

    protected void undoMove(GameMove move, List<Piece> deadPieces) {
        deadPieces = deadPieces.reversed();
        for (PieceMove pieceMove : move.moves().reversed()) {
            Piece revivedPiece = deadPieces.getFirst();
            deadPieces.removeFirst();
            this.undoMove(pieceMove, revivedPiece);
        }
    }

    protected boolean playerHasNoAvailableMove(Player p) {
        List<Piece> pieces = this.board.getAllPiecesOfTeam(p.getTeam());
        for (Piece piece : pieces) {
            if (piece.getPossibleMoves(this).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public List<Cell> getValidCells(Piece piece) {
        List<Cell> validCells = new ArrayList<>();

        Cell startCell = this.board.getCellOfPiece(piece);
        for (GameMove move : this.possibleMoves) {
            if (move.moves().getFirst().source().equals(startCell)) {
                validCells.add(move.moves().getFirst().destination());
            }
        }

        return validCells;
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

    public List<String> getMovesNotation() {
        return this.movesNotation;
    }

    public List<Player> getPlayers() {
        return this.players;
    }
}
