package models;

import models.pieces.*;
import structure.Observable;
import structure.Position;

import java.util.ArrayList;
import java.util.List;

public class Game extends Observable implements Runnable {
    private final ChessBoard chessBoard;
    private final List<Player> players;
    private final int playerCount;
    private Player actualPlayer;
    public Move move;
    private Piece promotionPiece;

    public Game(List<Player> players) {
        this.players     = players;
        this.playerCount = players.size();
        for (Player player : players) {
            player.setGame(this);
        }
        this.chessBoard = new ChessBoard(this);
    }

    @Override
    public void run() {
        this.playGame();
    }

    public void playGame() {
        initializePieces();
        while (!(this.gameEnded())) {
            this.actualPlayer = this.nextPlayer();
            if (!this.actualPlayer.hasLost()) {
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
        this.chessBoard.unmarkValidMoveCells();
        this.updateAll();
        System.out.println("c la fin");
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

    public boolean gameEnded() {
        if (this.actualPlayer == null) {
            return false;
        }

        int alivePlayers = 0;
        for (Player player : this.players) {
            if (!player.hasLost() && !isInStalemate(player)) {
                alivePlayers++;
            }
        }

        return alivePlayers == 1;
    }

    private Player nextPlayer() {
        return this.players.get((players.indexOf(this.actualPlayer)+1) % this.playerCount);
    }

    public List<Cell> getValidCells(Piece piece, Player p) {
        Position start = this.chessBoard.getPositionOfCell(piece.getCell());

        List<Cell> accessibleCells = piece.getAccessibleCells(this.chessBoard);
        List<Cell> validCells = new ArrayList<>();
        for (Cell cell : accessibleCells) {
            Position end = this.chessBoard.getPositionOfCell(cell);

            if (this.isValidMove(new Move(start, end), p)) {
                validCells.add(cell);
            }
        }

        return validCells;
    }

    private boolean isValidMove(Move m, Player p) {
        boolean isValid = false;

        Position sourcePosition      = m.source();
        Position destinationPosition = m.destination();
        Cell sourceCell      = this.chessBoard.getCell(sourcePosition);
        Cell destinationCell = this.chessBoard.getCell(destinationPosition);

        if (sourceCell.hasPiece()) {
            Piece pieceToMove = sourceCell.getPiece();
            if (pieceToMove.getTeam() == p.getTeam()) {
                List<Cell> accessibleCells = pieceToMove.getAccessibleCells(chessBoard);
                if (accessibleCells.contains(destinationCell)) {
                    Piece deadPiece = destinationCell.getPiece();
                    this.tryMove(m);
                    isValid = (!this.isInCheck(p));
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
                for (Cell c : piece.getAccessibleCells(chessBoard)) {
                    if (c.getPiece() instanceof King) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isInCheckIfMove(Move m) {
        Piece deadPiece = this.chessBoard.getCell(m.destination()).getPiece();
        this.tryMove(m);
        boolean isInCheck = this.isInCheck(this.actualPlayer);
        this.undoMove(m, deadPiece);
        return isInCheck;
    }

    public boolean isInCheckmate(Player p) {
        return (this.isInCheck(p) && this.playerHasNoAvailableMove(p));
    }

    public boolean isInStalemate(Player p) {
        return (!this.isInCheck(p) && this.playerHasNoAvailableMove(p));
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
        Cell startCell = this.chessBoard.getCell(m.source());
        Cell endCell   = this.chessBoard.getCell(m.destination());
        Piece movedPiece = startCell.getPiece();

        this.chessBoard.movePiece(null, startCell);
        this.chessBoard.movePiece(movedPiece, endCell);

        return movedPiece;
    }

    private void applyMove(Move m) {
        Piece movedPiece = this.tryMove(m);

        Cell destinationCell = this.getBoard().getCell(m.destination());
        checkCastling(destinationCell);
        checkPromotion(destinationCell);

        movedPiece.setHasMoved(true);
        this.chessBoard.unselectAll();
    }

    private void undoMove(Move m, Piece deadPiece) {
        Cell startCell = this.chessBoard.getCell(m.source());
        Cell endCell = this.chessBoard.getCell(m.destination());
        Piece movedPiece = endCell.getPiece();

        this.chessBoard.movePiece(movedPiece, startCell);
        this.chessBoard.movePiece(deadPiece, endCell);
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

    private void checkCastling(Cell destinationCell) {
        if (destinationCell.hasPiece() && destinationCell.getPiece().getPieceName().equals("king") && !destinationCell.getPiece().hasAlreadyMove()) {
            int kingY = this.getBoard().getPositionOfCell(destinationCell).getY();
            int kingX = this.getBoard().getPositionOfCell(destinationCell).getX();

            Position rookToMovePosition;
            Position whereMoveRookPosition;
            if (kingX == 2 || kingX == 6) {
                if (kingX == 2) {
                    rookToMovePosition    = new Position(0, kingY);
                    whereMoveRookPosition = new Position(3, kingY);
                } else {
                    rookToMovePosition    = new Position(7, kingY);
                    whereMoveRookPosition = new Position(5, kingY);
                }
                applyMove(new Move(rookToMovePosition, whereMoveRookPosition));
            }
        }
    }

    private void checkPromotion(Cell destinationCell) {
        if (destinationCell.hasPiece() && destinationCell.getPiece().getPieceName().equals("pawn")) {
            int pawnY = this.getBoard().getPositionOfCell(destinationCell).getY();
            int pawnTeam = destinationCell.getPiece().getTeam();

            if ((pawnY == 0 && pawnTeam == 0) || (pawnY == 7 && pawnTeam == 1)) {
                String[] s = new String[]{"promotion"};
                updateAllWithParams(s);

                int pawnX = this.getBoard().getPositionOfCell(destinationCell).getX();
                this.chessBoard.addPiece(this.promotionPiece, pawnX,pawnY);
            }
        }
    }
}
