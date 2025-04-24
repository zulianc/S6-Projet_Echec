package models;

import models.pieces.*;
import models.players.HumanPlayer;
import models.players.Player;
import structure.Observable;
import structure.Observer;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class Game extends Observable implements Runnable {
    private final ChessBoard chessBoard;
    private final List<Player> players;
    private final int playerCount;
    private Player actualPlayer;
    public Move move;
    private Piece promotionPiece;
    private int turn;
    private boolean isStaleMate = false;
    private List<String> moves = new ArrayList<>();

    public Game(List<Player> players) {
        this.players     = players;
        this.playerCount = players.size();
        for (Player player : players) {
            if (player instanceof Observer) {
                this.addObserver((Observer) player);
            }
            player.setGame(this);
        }
        this.chessBoard = new ChessBoard(this);
        this.turn = 0;
    }

    @Override
    public void run() {
        this.playGame();
    }

    public void playGame() {
        initializePieces();
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
                System.out.println(moves);
                this.checkIfPlayerLost(this.nextPlayer());
                this.updateAll();
            }
        }
        String lastMove = this.moves.getLast();
        if (!isStaleMate) {
            this.moves.set(this.moves.size()-1, lastMove.replace("+", "#"));
        } else {
            this.moves.add("1/2-1/2");
        }
        System.out.println(moves);
        System.out.println(PGNConverter.convertGameToPGN(this));
        String[] s = new String[]{"gameEnded"};
        updateAllWithParams(s);

        System.out.println("game ended");

        for (Player player : players) {
            synchronized (player) {
                player.notify();
            }
        }
    }

    public void sendMove(Move m) {
        this.move = m;
        synchronized (this) {
            notify();
        }
    }

    public void sendPromotion(String pieceName) {
        System.out.println("Promote to " + pieceName);
        this.promotionPiece = createPieceFromString(pieceName);
    }

    private Piece createPieceFromString(String pieceName) {
        Piece result;
        int actualPlayerTeam = this.actualPlayer.getTeam();
        switch (pieceName) {
            case "rook" : {
                result = new Rook(actualPlayerTeam);
                break;
            }
            case "bishop" : {
                result = new Bishop(actualPlayerTeam);
                break;
            }
            case "knight" : {
                result = new Knight(actualPlayerTeam);
                break;
            }
            default : {
                result = new Queen(actualPlayerTeam);
                break;
            }
        }
        return result;
    }

    private void initializePieces() {
        System.out.println("Initializing pieces...");
        this.chessBoard.addPiece(new Rook(1), 0, 0);
        this.chessBoard.addPiece(new Rook(1), 7, 0);
        this.chessBoard.addPiece(new Rook(0), 0, 7);
        this.chessBoard.addPiece(new Rook(0), 7, 7);

        this.chessBoard.addPiece(new Knight(1), 1, 0);
        this.chessBoard.addPiece(new Knight(1), 6, 0);
        this.chessBoard.addPiece(new Knight(0), 1, 7);
        this.chessBoard.addPiece(new Knight(0), 6, 7);

        this.chessBoard.addPiece(new Bishop(1), 2, 0);
        this.chessBoard.addPiece(new Bishop(1), 5, 0);
        this.chessBoard.addPiece(new Bishop(0), 2, 7);
        this.chessBoard.addPiece(new Bishop(0), 5, 7);

        this.chessBoard.addPiece(new Queen(1), 3, 0);
        this.chessBoard.addPiece(new Queen(0), 3, 7);

        this.chessBoard.addPiece(new King(1), 4, 0);
        this.chessBoard.addPiece(new King(0), 4, 7);

        for (int x = 0; x < 8; x++) {
            this.chessBoard.addPiece(new ChessPawn(1), x, 1);
        }
        for (int x = 0; x < 8; x++) {
            this.chessBoard.addPiece(new ChessPawn(0), x, 6);
        }
    }

    private void checkIfPlayerLost(Player p) {
        if (this.isInCheckmate(p)) {
            p.makePlayerLose();
        }
    }

    public boolean hasGameEnded() {
        if (this.actualPlayer == null) {
            return false;
        }

        int alivePlayers = 0;
        for (Player player : this.players) {
            if (player.isAlive() && !isInStalemate(player)) {
                alivePlayers++;
            }
        }

        return alivePlayers == 1;
    }

    private Player nextPlayer() {
        return this.players.get((players.indexOf(this.actualPlayer)+1) % this.playerCount);
    }

    public List<Cell> getValidCells(Piece piece, Player p) {
        Cell startCell = piece.getCell();

        List<Cell> accessibleCells = piece.getAccessibleCells(this.chessBoard);
        List<Cell> validCells = new ArrayList<>();
        for (Cell cell : accessibleCells) {

            if (this.isValidMove(new Move(startCell, cell), p)) {
                validCells.add(cell);
            }
        }

        return validCells;
    }

    private boolean isValidMove(Move m, Player p) {
        boolean isValid = false;

        Cell sourceCell      = m.source();
        Cell destinationCell = m.destination();

        if (sourceCell.hasPiece()) {
            Piece pieceToMove = sourceCell.getPiece();
            if (pieceToMove.getTeam() == p.getTeam()) {
                List<Cell> accessibleCells = pieceToMove.getAccessibleCells(chessBoard);
                if (accessibleCells.contains(destinationCell)) {
                    Piece deadPiece = destinationCell.getPiece();
                    this.tryMove(m);
                    isValid = !this.isInCheck(p);
                    this.undoMove(m, deadPiece);
                }
            }
        }

        return isValid;
    }

    private boolean isInCheck(Player p) {
        List<Piece> pieces = this.chessBoard.getAllPieces();
        for (Piece piece : pieces) {
            if (piece.getTeam() != p.getTeam()) {
                for (Cell cell : piece.getAccessibleCells(chessBoard)) {
                    if (cell.getPiece() instanceof King) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isInCheckIfMove(Move m) {
        Piece deadPiece = m.destination().getPiece();
        this.tryMove(m);
        boolean isInCheck = this.isInCheck(this.actualPlayer);
        this.undoMove(m, deadPiece);
        return isInCheck;
    }

    public boolean wouldBeInCheckIfMove(Move m) {
        Player actualPlayer = this.actualPlayer;
        Piece deadPiece = m.destination().getPiece();
        this.tryMove(m);
        boolean isInCheck = this.isInCheck(this.nextPlayer());
        this.undoMove(m, deadPiece);
        this.actualPlayer = actualPlayer;
        return isInCheck;
    }

    public boolean isInCheckmate(Player p) {
        boolean isInCheckmate = this.isInCheck(p) && this.playerHasNoAvailableMove(p);
        if (isInCheckmate) {
            this.isStaleMate = false;
        }

        return isInCheckmate;
    }

    public boolean isInStalemate(Player p) {
        boolean isInStalemate = !this.isInCheck(p) && this.playerHasNoAvailableMove(p);
        if (isInStalemate) {
            this.isStaleMate = true;
        }

        return isInStalemate;
    }

    private boolean playerHasNoAvailableMove(Player p) {
        List<Piece> pieces = this.chessBoard.getAllPieces();
        for (Piece piece : pieces) {
            if (piece.getTeam() == p.getTeam() && !this.getValidCells(piece, p).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private Piece tryMove(Move m) {
        Cell startCell = m.source();
        Cell endCell   = m.destination();
        Piece movedPiece = startCell.getPiece();

        this.checkTryingEnPassant(m);

        this.chessBoard.movePiece(null, startCell);
        this.chessBoard.movePiece(movedPiece, endCell);

        return movedPiece;
    }

    private void applyMove(Move m, boolean recordMove) {
        if (recordMove) this.moves.add(PGNConverter.convertMoveToPGN(chessBoard, m));

        Piece movedPiece = this.tryMove(m);

        Cell destinationCell = m.destination();
        checkCastling(m);
        checkPromotion(destinationCell);

        movedPiece.pieceHasMoved(this.turn);
        String[] s = new String[]{"unselectAll"};
        updateAllWithParams(s);
    }

    private void applyMove(Move m) {
        this.applyMove(m, true);
    }

    private void undoMove(Move m, Piece deadPiece) {
        Cell startCell = m.source();
        Cell endCell   = m.destination();
        Piece movedPiece = endCell.getPiece();

        this.checkUndoingEnPassant(m, deadPiece);

        this.chessBoard.movePiece(movedPiece, startCell);
        this.chessBoard.movePiece(deadPiece,  endCell);
    }

    private void checkCastling(Move m) {
        Cell destinationCell = m.destination();
        if (destinationCell.hasPiece() && destinationCell.getPiece().getPieceName().equals("king") && destinationCell.getPiece().hasNeverMoved()) {
            int kingY = this.getBoard().getPositionOfCell(destinationCell).getY();

            Cell rookToMoveCell;
            Cell whereMoveRookCell;
            if (Math.abs(this.chessBoard.getDistanceFromMove(m).getX()) == 2) {
                if (this.chessBoard.getDistanceFromMove(m).getX() < 0) {
                    rookToMoveCell    = this.chessBoard.getCell(0, kingY);
                    whereMoveRookCell = this.chessBoard.getCell(3, kingY);
                } else {
                    rookToMoveCell    = this.chessBoard.getCell(7, kingY);
                    whereMoveRookCell = this.chessBoard.getCell(5, kingY);
                }
                applyMove(new Move(rookToMoveCell, whereMoveRookCell), false);
            }
        }
    }

    private void checkPromotion(Cell destinationCell) {
        if (destinationCell.hasPiece() && destinationCell.getPiece().getPieceName().equals("pawn")) {
            int pawnY = this.getBoard().getPositionOfCell(destinationCell).getY();
            int pawnTeam = destinationCell.getPiece().getTeam();

            if ((pawnY == 0 && pawnTeam == 0) || (pawnY == 7 && pawnTeam == 1)) {
                String[] s;
                if (this.actualPlayer instanceof HumanPlayer) {
                    s = new String[]{"humanPromotion"};
                } else {
                    s = new String[]{"botPromotion"};
                }
                updateAllWithParams(s);

                int pawnX = this.getBoard().getPositionOfCell(destinationCell).getX();
                this.chessBoard.addPiece(this.promotionPiece, pawnX, pawnY);
            }
        }
    }

    private void checkTryingEnPassant(Move m) {
        Cell sourceCell = m.source();
        Cell destinationCell = m.destination();
        int sourceX = this.getBoard().getPositionOfCell(sourceCell).getX();
        int destinationX = this.getBoard().getPositionOfCell(destinationCell).getX();
        if (sourceCell.getPiece() instanceof ChessPawn && abs(sourceX - destinationX) == 1 && !destinationCell.hasPiece()) {
            int destinationY = this.getBoard().getPositionOfCell(destinationCell).getY();
            int pieceToRemoveY = (destinationY == 2) ? destinationY + 1 : destinationY - 1;
            Cell pieceToRemove = this.chessBoard.getCell(destinationX, pieceToRemoveY);
            this.chessBoard.movePiece(null, pieceToRemove);
        }
    }

    private void checkUndoingEnPassant(Move m, Piece deadPiece) {
        Cell sourceCell = m.source();
        Cell destinationCell = m.destination();
        int sourceX = this.getBoard().getPositionOfCell(sourceCell).getX();
        int destinationX = this.getBoard().getPositionOfCell(destinationCell).getX();
        if (destinationCell.getPiece() instanceof ChessPawn && abs(sourceX - destinationX) == 1 && deadPiece == null) {
            int destinationY = this.getBoard().getPositionOfCell(destinationCell).getY();
            int pieceToReviveY = (destinationY == 2) ? destinationY + 1 : destinationY - 1;
            Cell pieceToRevive = this.chessBoard.getCell(destinationX, pieceToReviveY);
            this.chessBoard.addPiece(new ChessPawn(1 - destinationCell.getPiece().getTeam()), pieceToRevive);
            pieceToRevive.getPiece().pieceHasMoved(this.turn - 1);
        }
    }

    public ChessBoard getBoard() {
        return this.chessBoard;
    }

    public int getPlayerCount() {
        return this.playerCount;
    }

    public Player getActualPlayer() {
        return this.actualPlayer;
    }

    public int getTurn() {
        return this.turn;
    }

    public boolean isStaleMate() {
        return isStaleMate;
    }

    public List<String> getMoves() {
        return this.moves;
    }

    public List<Player> getPlayers() {
        return this.players;
    }
}
