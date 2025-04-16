package models.players;

import models.Cell;
import models.Move;
import models.pieces.Piece;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomBotPlayer extends Player {

    public RandomBotPlayer(int team) {
        super(team);
        this.name = "Random Bot"+team;
    }

    @Override
    public Move getMove() {
        List<Piece> piecesToMove = this.game.getBoard().getAllPiecesOfTeam(this.team);
        List<Move> accessibleCells = new ArrayList<>();
        for (Piece piece : piecesToMove) {
            Cell sourceCell = piece.getCell();

            List<Cell> accessibleCellsForOnePiece = this.game.getValidCells(piece, this);
            if (accessibleCellsForOnePiece != null) {
                for (Cell destinationCell : accessibleCellsForOnePiece) {
                    Move moveToAdd = new Move(sourceCell, destinationCell);
                    accessibleCells.add(moveToAdd);
                }
            }
        }
        Random random = new Random();
        int max = accessibleCells.size();

        return accessibleCells.get(random.nextInt(max));
    }
}
