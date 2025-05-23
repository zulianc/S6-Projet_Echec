package models.players;

import models.boards.Cell;
import models.boards.PlayerMove;
import models.games.ChessGame;
import models.pieces.Piece;
import structure.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomBotPlayer extends Player implements Observer {

    public RandomBotPlayer(int team) {
        super("Random Bot " + team, team);
    }

    @Override
    public PlayerMove getMove() {
        List<Piece> piecesToMove = this.game.getBoard().getAllPiecesOfTeam(this.team);
        List<PlayerMove> accessibleCells = new ArrayList<>();
        for (Piece piece : piecesToMove) {
            Cell sourceCell = this.game.getBoard().getCellOfPiece(piece);

            List<Cell> accessibleCellsForOnePiece = this.game.getValidCells(piece);
            if (accessibleCellsForOnePiece != null) {
                for (Cell destinationCell : accessibleCellsForOnePiece) {
                    PlayerMove moveToAdd = new PlayerMove(sourceCell, destinationCell);
                    accessibleCells.add(moveToAdd);
                }
            }
        }
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Random random = new Random();
        int max = accessibleCells.size();

        return accessibleCells.get(random.nextInt(max));
    }

    @Override
    public void update() {
        //nothing to do
    }

    @Override
    public void updateParams(Object[] params) {
        if (params instanceof String[]) {
            String signal = (String) params[0];
            if (signal.equals("botPromotion")) {
                ((ChessGame) this.game).sendPromotion("queen");
            }
        }
    }
}
