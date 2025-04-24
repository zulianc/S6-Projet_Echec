package models.players;

import models.Cell;
import models.ChessBoard;
import models.Move;
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
        ChessBoard board = this.game.getBoard();

        setupMatrix();

        for (int x = 0; x < ChessBoard.CHESS_BOARD_SIZE; x++) {
            for (int y = 0; y < ChessBoard.CHESS_BOARD_SIZE; y++) {
                Piece currentPiece = board.getCell(x, y).getPiece();
                if (currentPiece != null && currentPiece.getTeam() != this.team) {
                    this.weightedMatrix[x][y] += currentPiece.getValue();
                }
            }
        }
    }

    @Override
    public Move getMove() {
        updateWeightedMatrix();

        List<Piece> piecesToMove = this.game.getBoard().getAllPiecesOfTeam(this.team);
        List<Move> accessibleCells = new ArrayList<>();
        List<Move> bestMoves = new ArrayList<>();
        int bestMoveValue = -1;
        for (Piece piece : piecesToMove) {
            Cell sourceCell = this.game.getBoard().getCellOfPiece(piece);

            List<Cell> accessibleCellsForOnePiece = this.game.getValidCells(piece, this);
            if (accessibleCellsForOnePiece != null) {
                for (Cell destinationCell : accessibleCellsForOnePiece) {
                    Move moveToAdd = new Move(sourceCell, destinationCell);
                    accessibleCells.add(moveToAdd);
                }
            }
        }

        for (Move possibleMove : accessibleCells) {
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
        for (int i = 0; i < ChessBoard.CHESS_BOARD_SIZE; i++) {
            for (int j = 0; j < ChessBoard.CHESS_BOARD_SIZE; j++) {
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
                this.game.sendPromotion("queen");
            }
        }
    }
}
