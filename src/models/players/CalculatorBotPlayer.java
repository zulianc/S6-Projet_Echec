package models.players;

import models.boards.Cell;
import models.boards.GameBoard;
import models.boards.PieceMove;
import models.games.ChessGame;
import models.pieces.Piece;
import structure.Observer;
import structure.Position2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CalculatorBotPlayer extends Player implements Observer {

    private int[][] weightedMatrix;

    public CalculatorBotPlayer(int team) {
        super("Calculator Bot " + team, team);
        setupMatrix();
    }

    private void setupMatrix() {
        this.weightedMatrix = new int[][] {
                {0, 1, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 2, 2, 1, 0, 0},
                {0, 0, 1, 2, 2, 1, 0, 0},
                {0, 0, 1, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 1, 0, 0},
        };
    }

    private void updateWeightedMatrix() {
        GameBoard board = this.game.getBoard();

        setupMatrix();

        for (int x = 0; x < this.weightedMatrix.length; x++) {
            for (int y = 0; y < this.weightedMatrix.length; y++) {
                Piece currentPiece = board.getCell(x, y).getPiece();
                if (currentPiece != null && currentPiece.getTeam() != this.team) {
                    this.weightedMatrix[x][y] += currentPiece.getValue();
                }
            }
        }
    }

    @Override
    public PieceMove getMove() {
        updateWeightedMatrix();

        List<Piece> piecesToMove = this.game.getBoard().getAllPiecesOfTeam(this.team);
        List<PieceMove> accessibleCells = new ArrayList<>();
        List<PieceMove> bestMoves = new ArrayList<>();
        int bestMoveValue = -1;
        for (Piece piece : piecesToMove) {
            Cell sourceCell = this.game.getBoard().getCellOfPiece(piece);

            List<Cell> accessibleCellsForOnePiece = this.game.getValidCells(piece, this);
            if (accessibleCellsForOnePiece != null) {
                for (Cell destinationCell : accessibleCellsForOnePiece) {
                    PieceMove moveToAdd = new PieceMove(sourceCell, destinationCell);
                    accessibleCells.add(moveToAdd);
                }
            }
        }

        for (PieceMove possibleMove : accessibleCells) {
            Position2D currentPosition = this.game.getBoard().getPositionOfCell(possibleMove.destination());
            int currentValue = this.weightedMatrix[currentPosition.getX()][currentPosition.getY()];
            if (currentValue > bestMoveValue) {
                bestMoves = new ArrayList<>();
                bestMoveValue = currentValue;
                bestMoves.add(possibleMove);
            } else if (currentValue == bestMoveValue) {
                bestMoves.add(possibleMove);
            }
        }

        printWeightedMatrix();

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Random random = new Random();
        int max = bestMoves.size();

        return bestMoves.get(random.nextInt(max));
    }

    private void printWeightedMatrix() {
        System.out.println("Weighted Matrix : ");
        for (int i = 0; i < this.weightedMatrix.length; i++) {
            for (int j = 0; j < this.weightedMatrix.length; j++) {
                System.out.print(this.weightedMatrix[i][j] + " ");
            }
            System.out.println();
        }
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
