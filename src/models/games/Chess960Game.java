package models.games;

import models.pieces.chess.*;
import models.players.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.random.RandomGenerator;

public class Chess960Game extends ChessGame {
    public Chess960Game(List<Player> players) {
        super(players);
    }

    @Override
    protected void initializePieces() {
        List<Integer> columns = new LinkedList<>();

        for (int x = 0; x < 8; x++) {
            this.board.setPieceToCell(new ChessPawn(1), x, 1);
            this.board.setPieceToCell(new ChessPawn(0), x, 6);
            columns.add(x);
        }

        Integer firstBishopPosition = (this.getRandomUInt() % 4) * 2;
        Integer secondBishopPosition = (this.getRandomUInt() % 4) * 2 + 1;
        columns.remove(firstBishopPosition);
        columns.remove(secondBishopPosition);

        this.board.setPieceToCell(new Bishop(1), firstBishopPosition, 0);
        this.board.setPieceToCell(new Bishop(1), secondBishopPosition, 0);
        this.board.setPieceToCell(new Bishop(0), firstBishopPosition, 7);
        this.board.setPieceToCell(new Bishop(0), secondBishopPosition, 7);

        for (int i = 0; i < 2; i++) {
            Integer knightPosition = columns.get(this.getRandomUInt() % columns.size());
            columns.remove(knightPosition);

            this.board.setPieceToCell(new Knight(1), knightPosition, 0);
            this.board.setPieceToCell(new Knight(0), knightPosition, 7);
        }

        Integer queenPosition = columns.get(this.getRandomUInt() % columns.size());
        columns.remove(queenPosition);

        this.board.setPieceToCell(new Queen(1), queenPosition, 0);
        this.board.setPieceToCell(new Queen(0), queenPosition, 7);

        Integer firstRookPosition = columns.get(0);
        Integer kingPosition = columns.get(1);
        Integer secondRookPosition = columns.get(2);

        this.board.setPieceToCell(new Rook(1), firstRookPosition, 0);
        this.board.setPieceToCell(new Rook(1), secondRookPosition, 0);
        this.board.setPieceToCell(new Rook(0), firstRookPosition, 7);
        this.board.setPieceToCell(new Rook(0), secondRookPosition, 7);

        this.board.setPieceToCell(new King(1), kingPosition, 0);
        this.board.setPieceToCell(new King(0), kingPosition, 7);
    }

    protected int getRandomUInt() {
        return Math.abs(RandomGenerator.getDefault().nextInt());
    }
}
