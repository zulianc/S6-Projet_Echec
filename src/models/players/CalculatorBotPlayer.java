package models.players;

import models.Cell;
import models.ChessBoard;
import models.Move;
import models.pieces.Piece;
import structure.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CalculatorBotPlayer extends Player {

    private int[][] weightedMatrix;

    public CalculatorBotPlayer(int team) {
        super(team);
        this.name = "Calculator Bot"+team;

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
            Cell sourceCell = piece.getCell();

            List<Cell> accessibleCellsForOnePiece = this.game.getValidCells(piece, this);
            if (accessibleCellsForOnePiece != null) {
                for (Cell destinationCell : accessibleCellsForOnePiece) {
                    Move moveToAdd = new Move(sourceCell, destinationCell);
                    accessibleCells.add(moveToAdd);
                }
            }
        }

        for (Move possibleMove : accessibleCells) {
            Position currentPosition = this.game.getBoard().getPositionOfCell(possibleMove.destination());
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
}
