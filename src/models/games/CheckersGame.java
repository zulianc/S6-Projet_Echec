package models.games;

import models.boards.Cell;
import models.boards.GameMove;
import models.boards.PlayerMove;
import models.pieces.Piece;
import models.pieces.checkers.CheckerPawn;
import models.pieces.checkers.CheckerQueen;
import models.players.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CheckersGame extends Game {
    public CheckersGame(List<Player> players) {
        super(players, 2, 10);
    }

    @Override
    protected void initializePieces() {
        for (int y = 0; y < 4; y++) {
            for (int i = 0; i < 5; i++) {
                int x = (y % 2 == 0) ? (i * 2 + 1) : (i * 2);

                this.board.setPieceToCell(new CheckerPawn(1), x, y);
                this.board.setPieceToCell(new CheckerPawn(0), x, y + 6);
            }
        }
    }

    @Override
    protected void getPossibleMoves() {
        this.possibleMoves = new ArrayList<>();

        int maximumSizeChain = 0;
        for (Piece piece : this.board.getAllPiecesOfTeam(this.actualPlayer.getTeam())) {
            for (GameMove move : piece.getPossibleMoves(this)) {
                if (maximumSizeChain < move.moves().size()) {
                    maximumSizeChain = move.moves().size();
                }

                this.possibleMoves.add(move);
            }
        }

        Iterator<GameMove> iterator = this.possibleMoves.iterator();
        while (iterator.hasNext()) {
            GameMove move = iterator.next();
            if (move.moves().size() < maximumSizeChain) {
                iterator.remove();
            }
        }
    }

    @Override
    protected void checkSpecialRules() {
        Cell destinationCell = this.currentMove.moves().getLast().destination();
        if (!destinationCell.hasPiece()) {
            return;
        }

        Piece piece = destinationCell.getPiece();

        int cellY = this.board.getPositionOfCell(destinationCell).getY();
        if ((cellY == 0 && piece instanceof CheckerPawn && piece.getTeam() == 0)
            || (cellY == 9 && piece instanceof CheckerPawn && piece.getTeam() == 1)) {

            Piece promotedPiece = new CheckerQueen(piece.getTeam());
            this.board.removePieceFromBoard(piece);
            this.board.setPieceToCell(promotedPiece, destinationCell);
        }
    }

    @Override
    protected void updateNotation() {
        //TODO notation
    }

    @Override
    protected void updateNotationLastMove() {
        //TODO notation
    }

    @Override
    protected void checkIfPlayerLost(Player p) {
        if (this.board.getAllPiecesOfTeam(p.getTeam()).isEmpty() || this.playerHasNoAvailableMove(p)) {
            p.playerLostGame();
        }
    }

    @Override
    public void checkIfGameEnded() {
        int alivePlayers = 0;
        for (Player player : this.players) {
            if (player.isAlive()) {
                alivePlayers++;
            }
        }

        this.gameEnded = (alivePlayers < 2);
    }
}
