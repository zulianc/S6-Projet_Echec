package models.games;

import models.boards.Cell;
import models.boards.Move;
import models.pieces.*;
import models.pieces.chess.*;
import models.players.HumanPlayer;
import models.players.Player;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class ChessGame extends Game {

    public ChessGame(List<Player> players) {
        super(players);
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

    @Override
    protected void initializePieces() {
        System.out.println("Initializing pieces...");
        this.chessBoard.setPieceToCell(new Rook(1), 0, 0);
        this.chessBoard.setPieceToCell(new Rook(1), 7, 0);
        this.chessBoard.setPieceToCell(new Rook(0), 0, 7);
        this.chessBoard.setPieceToCell(new Rook(0), 7, 7);

        this.chessBoard.setPieceToCell(new Knight(1), 1, 0);
        this.chessBoard.setPieceToCell(new Knight(1), 6, 0);
        this.chessBoard.setPieceToCell(new Knight(0), 1, 7);
        this.chessBoard.setPieceToCell(new Knight(0), 6, 7);

        this.chessBoard.setPieceToCell(new Bishop(1), 2, 0);
        this.chessBoard.setPieceToCell(new Bishop(1), 5, 0);
        this.chessBoard.setPieceToCell(new Bishop(0), 2, 7);
        this.chessBoard.setPieceToCell(new Bishop(0), 5, 7);

        this.chessBoard.setPieceToCell(new Queen(1), 3, 0);
        this.chessBoard.setPieceToCell(new Queen(0), 3, 7);

        this.chessBoard.setPieceToCell(new King(1), 4, 0);
        this.chessBoard.setPieceToCell(new King(0), 4, 7);

        for (int x = 0; x < 8; x++) {
            this.chessBoard.setPieceToCell(new ChessPawn(1), x, 1);
        }
        for (int x = 0; x < 8; x++) {
            this.chessBoard.setPieceToCell(new ChessPawn(0), x, 6);
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
    protected boolean isValidMove(Move m, Player p) {
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

    private boolean isInCheck(Player p) {
        List<Piece> pieces = this.chessBoard.getAllPieces();
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

    public boolean isntInCheckIfMove(Move m) {
        Piece deadPiece = m.destination().getPiece();
        this.tryMove(m);
        boolean isInCheck = this.isInCheck(this.actualPlayer);
        this.undoMove(m, deadPiece);
        return !isInCheck;
    }

    public boolean isInCheckmate(Player p) {
        return this.isInCheck(p) && this.playerHasNoAvailableMove(p);
    }

    public boolean isInStalemate(Player p) {
        return !this.isInCheck(p) && this.playerHasNoAvailableMove(p);
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

        this.chessBoard.removePieceFromCell(endCell.getPiece());
        this.chessBoard.setPieceToCell(null, startCell);
        this.chessBoard.setPieceToCell(movedPiece, endCell);

        return movedPiece;
    }

    @Override
    protected void applyMove(Move m) {
        Piece movedPiece = this.tryMove(m);

        Cell destinationCell = m.destination();
        checkCastling(destinationCell);
        checkPromotion(destinationCell);

        movedPiece.signalPieceJustMoved(this.turn);
        String[] s = new String[]{"unselectAll"};
        updateAllWithParams(s);
    }

    private void undoMove(Move m, Piece deadPiece) {
        Cell startCell = m.source();
        Cell endCell   = m.destination();
        Piece movedPiece = endCell.getPiece();

        this.checkUndoingEnPassant(m, deadPiece);

        this.chessBoard.setPieceToCell(movedPiece, startCell);
        this.chessBoard.setPieceToCell(deadPiece,  endCell);
    }

    private void checkCastling(Cell destinationCell) {
        if (destinationCell.hasPiece() && destinationCell.getPiece().getPieceName().equals("king") && destinationCell.getPiece().hasNeverMoved()) {
            int kingY = this.getBoard().getPositionOfCell(destinationCell).getY();
            int kingX = this.getBoard().getPositionOfCell(destinationCell).getX();

            Cell rookToMoveCell;
            Cell whereMoveRookCell;
            if (kingX == 2 || kingX == 6) {
                if (kingX == 2) {
                    rookToMoveCell    = this.chessBoard.getCell(0, kingY);
                    whereMoveRookCell = this.chessBoard.getCell(3, kingY);
                } else {
                    rookToMoveCell    = this.chessBoard.getCell(7, kingY);
                    whereMoveRookCell = this.chessBoard.getCell(5, kingY);
                }
                applyMove(new Move(rookToMoveCell, whereMoveRookCell));
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
                this.chessBoard.setPieceToCell(this.promotionPiece, pawnX, pawnY);
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
            this.chessBoard.setPieceToCell(null, pieceToRemove);
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
            this.chessBoard.setPieceToCell(new ChessPawn(1 - destinationCell.getPiece().getTeam()), pieceToRevive);
            pieceToRevive.getPiece().signalPieceJustMoved(this.turn - 1);
        }
    }

    @Override
    public List<Cell> getValidCells(Piece piece, Player p) {
        Cell startCell = this.chessBoard.getCellOfPiece(piece);

        List<Cell> accessibleCells = piece.getAccessibleCells(this);
        List<Cell> validCells = new ArrayList<>();
        for (Cell cell : accessibleCells) {
            if (this.isValidMove(new Move(startCell, cell), p)) {
                validCells.add(cell);
            }
        }

        return validCells;
    }
}
