package models.pieces;

import models.Cell;
import models.ChessBoard;
import models.decorators.AccessibleCellsDecorator;

import java.util.List;

public abstract class Piece {
    private final int team;
    private Cell cell;
    private final AccessibleCellsDecorator decorator;

    public Piece(int team, AccessibleCellsDecorator decorator) {
        this.team = team;
        this.decorator = decorator;
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

    @Override
    public String toString() {
        return "Piece{" +
                "team=" + team +
                '}';
    }
}
