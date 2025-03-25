package models;

import views.VCell;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChessBoard extends Observable {
    public static final int CHESS_BOARD_SIZE = 8;
    private final List<Cell> cells = new ArrayList<>();

    public ChessBoard() {
        generateCells();
    }

    private void generateCells() {
        int baseColor;
        for (int y = 0; y < ChessBoard.CHESS_BOARD_SIZE; y++) {
            for (int x = 0; x < ChessBoard.CHESS_BOARD_SIZE; x++) {
                baseColor = ((x+y) % 2 == 0) ? 0 : 1 ;
                this.cells.add(new Cell(baseColor));
            }
        }
    }

    public Cell getCell(int index) {
        return cells.get(index);
    }

    public void toggleSelect(Cell cell) {
        cell.toggleSelect();
        updateAll();
    }
}
