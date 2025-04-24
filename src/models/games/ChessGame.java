package models.games;

import models.PGNConverter;
import models.boards.Cell;
import models.boards.PieceMove;
import models.pieces.*;
import models.pieces.chess.*;
import models.players.HumanPlayer;
import models.players.Player;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class ChessGame extends Game {
    protected Piece promotionPiece;

    public ChessGame(List<Player> players) {
        super(players);
    }

    @Override
    public List<Cell> getValidCells(Piece piece, Player p) {
        Cell startCell = this.board.getCellOfPiece(piece);

        List<Cell> accessibleCells = piece.getAccessibleCells(this);
        List<Cell> validCells = new ArrayList<>();
        for (Cell cell : accessibleCells) {
            if (this.isValidMove(new PieceMove(startCell, cell), p)) {
                validCells.add(cell);
            }
        }

        return validCells;
    }

    @Override
    protected void initializePieces() {
        System.out.println("Initializing pieces...");
        this.board.setPieceToCell(new Rook(1), 0, 0);
        this.board.setPieceToCell(new Rook(1), 7, 0);
        this.board.setPieceToCell(new Rook(0), 0, 7);
        this.board.setPieceToCell(new Rook(0), 7, 7);

        this.board.setPieceToCell(new Knight(1), 1, 0);
        this.board.setPieceToCell(new Knight(1), 6, 0);
        this.board.setPieceToCell(new Knight(0), 1, 7);
        this.board.setPieceToCell(new Knight(0), 6, 7);

        this.board.setPieceToCell(new Bishop(1), 2, 0);
        this.board.setPieceToCell(new Bishop(1), 5, 0);
        this.board.setPieceToCell(new Bishop(0), 2, 7);
        this.board.setPieceToCell(new Bishop(0), 5, 7);

        this.board.setPieceToCell(new Queen(1), 3, 0);
        this.board.setPieceToCell(new Queen(0), 3, 7);

        this.board.setPieceToCell(new King(1), 4, 0);
        this.board.setPieceToCell(new King(0), 4, 7);

        for (int x = 0; x < 8; x++) {
            this.board.setPieceToCell(new ChessPawn(1), x, 1);
        }
        for (int x = 0; x < 8; x++) {
            this.board.setPieceToCell(new ChessPawn(0), x, 6);
        }
    }

    @Override
    protected void checkIfPlayerLost(Player p) {
        if (this.isInCheckmate(p)) {
            p.playerLostGame();
        }
    }

    @Override
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

    @Override
    protected boolean isValidMove(PieceMove m, Player p) {
        boolean isValid = false;

        Cell sourceCell      = m.source();
        Cell destinationCell = m.destination();

        if (sourceCell.hasPiece()) {
            Piece pieceToMove = sourceCell.getPiece();
            if (pieceToMove.getTeam() == p.getTeam()) {
                List<Cell> accessibleCells = pieceToMove.getAccessibleCells(this);
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

    @Override
    protected void applyMove(PieceMove m) {
        this.applyMove(m, true);
    }

    private void applyMove(PieceMove m, boolean recordMove) {
        if (recordMove) this.moves.add(PGNConverter.convertMoveToPGN(this, m));

        Piece movedPiece = this.tryMove(m);

        Cell destinationCell = m.destination();
        checkCastling(m);
        checkPromotion(destinationCell);

        movedPiece.signalPieceJustMoved(this.turn);
        String[] s = new String[]{"unselectAll"};
        updateAllWithParams(s);
    }

    private Piece tryMove(PieceMove m) {
        Cell startCell = m.source();
        Cell endCell   = m.destination();
        Piece movedPiece = startCell.getPiece();

        this.checkTryingEnPassant(m);

        this.board.removePieceFromCell(endCell.getPiece());
        this.board.setPieceToCell(null, startCell);
        this.board.setPieceToCell(movedPiece, endCell);

        return movedPiece;
    }

    private void undoMove(PieceMove m, Piece deadPiece) {
        Cell startCell = m.source();
        Cell endCell   = m.destination();
        Piece movedPiece = endCell.getPiece();

        this.checkUndoingEnPassant(m, deadPiece);

        this.board.setPieceToCell(movedPiece, startCell);
        this.board.setPieceToCell(deadPiece,  endCell);
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

    private boolean isInCheck(Player p) {
        List<Piece> pieces = this.board.getAllPieces();
        for (Piece piece : pieces) {
            if (piece.getTeam() != p.getTeam()) {
                for (Cell cell : piece.getAccessibleCells(this)) {
                    if (cell.getPiece() instanceof King) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isntInCheckIfMove(PieceMove m) {
        Piece deadPiece = m.destination().getPiece();
        this.tryMove(m);
        boolean isInCheck = this.isInCheck(this.actualPlayer);
        this.undoMove(m, deadPiece);
        return !isInCheck;
    }

    public boolean wouldBeInCheckIfMove(PieceMove m) {
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
        List<Piece> pieces = this.board.getAllPieces();
        for (Piece piece : pieces) {
            if (piece.getTeam() == p.getTeam() && !this.getValidCells(piece, p).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void checkCastling(PieceMove m) {
        Cell destinationCell = m.destination();
        if (destinationCell.hasPiece() && destinationCell.getPiece().getPieceName().equals("king") && destinationCell.getPiece().hasNeverMoved()) {
            int kingY = this.getBoard().getPositionOfCell(destinationCell).getY();

            Cell rookToMoveCell;
            Cell whereMoveRookCell;
            if (Math.abs(this.board.getDistanceFromMove(m).getX()) == 2) {
                if (this.board.getDistanceFromMove(m).getX() < 0) {
                    rookToMoveCell    = this.board.getCell(0, kingY);
                    whereMoveRookCell = this.board.getCell(3, kingY);
                } else {
                    rookToMoveCell    = this.board.getCell(7, kingY);
                    whereMoveRookCell = this.board.getCell(5, kingY);
                }
                applyMove(new PieceMove(rookToMoveCell, whereMoveRookCell), false);
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
                this.board.setPieceToCell(this.promotionPiece, pawnX, pawnY);
            }
        }
    }

    private void checkTryingEnPassant(PieceMove m) {
        Cell sourceCell = m.source();
        Cell destinationCell = m.destination();
        int sourceX = this.getBoard().getPositionOfCell(sourceCell).getX();
        int destinationX = this.getBoard().getPositionOfCell(destinationCell).getX();
        if (sourceCell.getPiece() instanceof ChessPawn && abs(sourceX - destinationX) == 1 && !destinationCell.hasPiece()) {
            int destinationY = this.getBoard().getPositionOfCell(destinationCell).getY();
            int pieceToRemoveY = (destinationY == 2) ? destinationY + 1 : destinationY - 1;
            Cell pieceToRemove = this.board.getCell(destinationX, pieceToRemoveY);
            this.board.setPieceToCell(null, pieceToRemove);
        }
    }

    private void checkUndoingEnPassant(PieceMove m, Piece deadPiece) {
        Cell sourceCell = m.source();
        Cell destinationCell = m.destination();
        int sourceX = this.getBoard().getPositionOfCell(sourceCell).getX();
        int destinationX = this.getBoard().getPositionOfCell(destinationCell).getX();
        if (destinationCell.getPiece() instanceof ChessPawn && abs(sourceX - destinationX) == 1 && deadPiece == null) {
            int destinationY = this.getBoard().getPositionOfCell(destinationCell).getY();
            int pieceToReviveY = (destinationY == 2) ? destinationY + 1 : destinationY - 1;
            Cell pieceToRevive = this.board.getCell(destinationX, pieceToReviveY);
            this.board.setPieceToCell(new ChessPawn(1 - destinationCell.getPiece().getTeam()), pieceToRevive);
            pieceToRevive.getPiece().signalPieceJustMoved(this.turn - 1);
        }
    }
}
