package models.games;

import models.PGNConverter;
import models.boards.Cell;
import models.boards.GameMove;
import models.boards.PlayerMove;
import models.pieces.*;
import models.pieces.chess.*;
import models.players.HumanPlayer;
import models.players.Player;

import java.util.ArrayList;
import java.util.List;

public class ChessGame extends Game {
    protected Piece promotionPiece;

    public ChessGame(List<Player> players) {
        super(players, 2, 8);
    }

    @Override
    protected void initializePieces() {
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
            this.board.setPieceToCell(new ChessPawn(0), x, 6);
        }
    }

    @Override
    protected void checkSpecialRules(PlayerMove playerMove) {
        Cell destinationCell = playerMove.destination();
        checkPromotion(destinationCell);
    }

    @Override
    protected void getPossibleMoves() {
        this.possibleMoves = new ArrayList<>();
        for (Piece piece : this.board.getAllPiecesOfTeam(this.actualPlayer.getTeam())) {
            for (GameMove move : piece.getPossibleMoves(this)) {
                List<Piece> deadPieces = this.doMove(move);
                boolean isValid = !this.isInCheck(this.actualPlayer);
                this.undoMove(move, deadPieces);

                if (isValid) {
                    this.possibleMoves.add(move);
                }
            }
        }
    }

    @Override
    protected void updateNotation(PlayerMove playerMove) {
        this.movesNotation.add(PGNConverter.convertMoveToPGN(this, playerMove));
    }

    @Override
    protected void updateNotationLastMove() {
        String lastMove = this.movesNotation.getLast();
        if (!this.isDraw()) {
            this.movesNotation.set(this.movesNotation.size()-1, lastMove.replace("+", "#"));
        } else {
            this.movesNotation.add("1/2-1/2");
        }
        System.out.println(movesNotation);
        System.out.println(PGNConverter.convertGameToPGN(this));
    }

    @Override
    protected void checkIfPlayerLost(Player p) {
        if (this.isInCheckmate(p)) {
            p.playerLostGame();
        }
    }

    @Override
    public void checkIfGameEnded() {
        int alivePlayers = 0;
        for (Player player : this.players) {
            if (player.isAlive() && !isInStalemate(player)) {
                alivePlayers++;
            }
        }

        this.gameEnded = (alivePlayers < 2);
    }

    public void sendPromotion(String pieceName) {
        System.out.println("Promote to " + pieceName);
        this.promotionPiece = createPieceFromString(pieceName);
    }

    protected void checkPromotion(Cell destinationCell) {
        if (destinationCell.hasPiece() && destinationCell.getPiece() instanceof ChessPawn) {
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

                this.board.removePieceFromBoard(destinationCell.getPiece());
                this.board.setPieceToCell(this.promotionPiece, destinationCell);
            }
        }
    }

    protected Piece createPieceFromString(String pieceName) {
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

    protected boolean isInCheck(Player p) {
        List<Piece> pieces = this.board.getAllPieces();
        for (Piece piece : pieces) {
            if (piece.getTeam() != p.getTeam()) {
                List<GameMove> possibleMoves = piece.getPossibleMoves(this);
                for (GameMove possibleMove : possibleMoves) {
                    Cell cell = possibleMove.moves().getFirst().destination();
                    if (cell.getPiece() instanceof King) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    protected boolean isPlayerInCheckIfMove(PlayerMove playerMove, Player p) {
        GameMove moveToDo = null;
        for (GameMove move : this.possibleMoves) {
            if (playerMove.correspondsTo(move)) {
                moveToDo = move;
            }
        }

        if (moveToDo == null) {
            return false;
        }

        List<Piece> deadPieces = this.doMove(moveToDo);
        boolean isInCheck = this.isInCheck(p);
        this.undoMove(moveToDo, deadPieces);

        return !isInCheck;
    }

    public boolean isInCheckIfMove(PlayerMove m) {
        return this.isPlayerInCheckIfMove(m, this.actualPlayer);
    }

    public boolean isNextPlayerInCheckIfMove(PlayerMove m) {
        return this.isPlayerInCheckIfMove(m, this.nextPlayer());
    }

    public boolean isInCheckmate(Player p) {
        return this.isInCheck(p) && this.playerHasNoAvailableMove(p);
    }

    public boolean isInStalemate(Player p) {
        return !this.isInCheck(p) && this.playerHasNoAvailableMove(p);
    }

    public boolean isCastling(PlayerMove m) {
        return m.source().getPiece() instanceof King && Math.abs(this.board.getDistanceFromMove(m).getX()) == 2;
    }
}
