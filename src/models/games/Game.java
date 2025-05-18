package models.games;

import models.boards.*;
import models.pieces.Piece;
import models.players.Player;
import structure.Observable;
import structure.Observer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class Game extends Observable implements Runnable {
    protected final GameBoard board;
    protected final List<Player> players;
    protected Player actualPlayer;
    protected int turn;
    protected boolean gameEnded;
    protected List<GameMove> possibleMoves;
    protected List<String> movesNotation;
    protected GameMove currentMove;
    public PlayerMove playerMove;

    public Game(List<Player> players) {
        this(players, -1, -1);
    }

    protected Game(List<Player> players, int requiredPlayerNumber, int boardSize) {
        if (players.size() != requiredPlayerNumber) {
            throw new IllegalArgumentException("The number of players must be equal to " + requiredPlayerNumber);
        }
        this.players = players;
        for (Player player : players) {
            if (player instanceof Observer) {
                this.addObserver((Observer) player);
            }
            player.startGame(this);
        }

        this.board = new GameBoard(boardSize);
        this.actualPlayer = null;
        this.turn = 0;
        this.gameEnded = false;

        this.possibleMoves = new ArrayList<>();
        this.movesNotation = new ArrayList<>();
    }

    @Override
    public void run() {
        this.playGame();
    }

    public void playGame() {
        System.out.println("Initializing pieces...");
        this.initializePieces();

        do {
            this.turn++;
            this.actualPlayer = this.nextPlayer();

            if (this.actualPlayer.isAlive()) {
                this.possibleMoves = this.getPossibleMoves(this.actualPlayer);
                this.currentMove = new GameMove();

                PlayerMove playerMove;
                do {
                    do {
                        this.updateAll();
                        playerMove = this.actualPlayer.getMove();
                    } while (!this.isValidPlayerMove(playerMove));
                    this.applyMove(playerMove);

                } while (!this.possibleMoves.isEmpty());
                this.checkSpecialRules();

                this.checkIfPlayerLost(this.nextPlayer());
                this.checkIfGameEnded();
                this.updateAll();
            }
        } while (!this.gameEnded);

        this.updateNotationLastMove();
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

    protected abstract List<GameMove> getPossibleMoves(Player p);

    protected abstract void checkSpecialRules();

    protected abstract void updateNotation(PlayerMove playerMove);

    protected abstract void updateNotationLastMove();

    protected abstract void checkIfPlayerLost(Player p);

    public abstract void checkIfGameEnded();

    public Player nextPlayer() {
        return this.players.get((players.indexOf(this.actualPlayer)+1) % this.players.size());
    }

    protected boolean isValidPlayerMove(PlayerMove playerMove) {
        for (GameMove move : this.possibleMoves) {
            if (playerMove.correspondsTo(move)) {
                return true;
            }
        }
        return false;
    }

    public void forceMove(PlayerMove playerMove) {
        this.turn++;
        this.possibleMoves = this.getPossibleMoves(this.actualPlayer);

        GameMove moveToDo = null;
        for (GameMove move : this.possibleMoves) {
            if (playerMove.correspondsTo(move)) {
                moveToDo = move;
            }
        }
        this.doMove(moveToDo);

        this.actualPlayer = this.nextPlayer();
        this.updateAll();
    }

    protected void applyMove(PlayerMove playerMove) {
        this.updateNotation(playerMove);

        PieceMove moveToDo = this.updatePossibleMoves(playerMove);
        if (moveToDo == null) {
            throw new RuntimeException("The move the player inputed isn't in the list of possible moves.");
        }
        doMove(moveToDo);

        boolean autoMove = true;
        while (!this.possibleMoves.isEmpty() && autoMove) {
            autoMove = true;
            PieceMove firstMove = this.possibleMoves.getFirst().moves().getFirst();
            for (GameMove move : this.possibleMoves) {
                if (!firstMove.equals(move.moves().getFirst())) {
                    autoMove = false;
                }
            }

            if (autoMove) {
                this.updatePossibleMoves(new PlayerMove(firstMove));
                doMove(firstMove);
            }
        }

        String[] s = new String[]{"unselectAll"};
        updateAllWithParams(s);
    }

    protected PieceMove updatePossibleMoves(PlayerMove playerMove) {
        Iterator<GameMove> it = this.possibleMoves.iterator();
        PieceMove moveToDo = null;
        while (it.hasNext()) {
            GameMove possibleMove = it.next();
            if (playerMove.correspondsTo(possibleMove)) {
                moveToDo = possibleMove.moves().getFirst();
                possibleMove.moves().removeFirst();
                if (possibleMove.moves().isEmpty()) {
                    it.remove();
                }
            }
            else {
                it.remove();
            }
        }

        this.currentMove.moves().addLast(moveToDo);
        return moveToDo;
    }

    protected Piece doMove(PieceMove move) {
        Piece movedPiece = move.source().getPiece();
        Piece deadPiece = move.captured().getPiece();

        if (!(move.destination().hasPiece() && move.destination().getPiece().getTeam() == movedPiece.getTeam())) {
            this.board.removePieceFromBoard(deadPiece);
            this.board.setPieceToCell(null, move.source());
            this.board.setPieceToCell(movedPiece, move.destination());

            movedPiece.signalPieceJustMoved(this.turn);
        }

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
        if (!move.source().hasPiece()) {
            Piece movedPiece = move.destination().getPiece();

            this.board.removePieceFromBoard(movedPiece);
            this.board.setPieceToCell(movedPiece, move.source());
            this.board.setPieceToCell(deadPiece, move.captured());

            movedPiece.signalPieceUnmoved();
        }
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
        return this.getPossibleMoves(p).isEmpty();
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

    public boolean hasGameEnded() {
        return this.gameEnded;
    }

    public List<String> getMovesNotation() {
        return this.movesNotation;
    }

    public List<Player> getPlayers() {
        return this.players;
    }
}
