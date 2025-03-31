package models.pieces;

import models.Cell;
import models.ChessBoard;
import models.decorators.AccessiblesCellsDecorator;

import java.util.ArrayList;

public abstract class Piece {
    private final int color;
    private Cell cell;
    private final AccessiblesCellsDecorator decorator;

    public Piece(int color, AccessiblesCellsDecorator decorator) {
        this.color = color;
        this.decorator = decorator;
    }

    public int getColor() {
        return color;
    }

    public abstract String getPieceName();

    public void goToCell(Cell cell) {
        this.cell = cell;
    }

    public ArrayList<Cell> getAccessibleCells(ChessBoard chessBoard) {
        return decorator.getAccessiblesCells(chessBoard, this.cell);
    }

    @Override
    public String toString() {
        return "Piece{" +
                "color=" + color +
                '}';
    }
}
