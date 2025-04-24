package models.pieces;

import models.Cell;
import models.ChessBoard;
import models.decorators.AccessibleCellsDecorator;

import java.util.List;

public abstract class Piece {
    private final int team;
    private Cell cell;
    private final AccessibleCellsDecorator decorator;
    private int moveCount;
    private int lastMoveTurn;
    protected int value;

    public Piece(int team, AccessibleCellsDecorator decorator, int value) {
        this.team = team;
        this.decorator = decorator;
        this.value = value;
        this.lastMoveTurn = -1;
        this.moveCount = 0;
    }

    public Piece(int team, AccessibleCellsDecorator decorator) {
        this(team, decorator, 1);
    }

    public int getTeam() {
        return team;
    }

    public abstract String getPieceName();

    public void goToCell(Cell cell) {
        this.cell = cell;
    }

    public List<Cell> getAccessibleCells(ChessBoard chessBoard) {
        return decorator.getAccessibleCells(chessBoard, this.cell);
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public boolean hasNeverMoved() {
        return this.moveCount == 0;
    }

    public int getMoveCount() {
        return this.moveCount;
    }

    public void pieceHasMoved(int turn) {
        this.moveCount++;
        this.lastMoveTurn = turn;
    }

    public Integer getLastMoveTurn() {
        if (this.lastMoveTurn == -1) {
            return null;
        }
        return this.lastMoveTurn;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "Piece{" +
                "team=" + team +
                "value=" + value +
                '}';
    }

    public abstract String getPieceCode();
}
