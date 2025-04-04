package models;

import models.pieces.Piece;
import structure.Position;

public class ChessBoard {
    public static final int CHESS_BOARD_SIZE = 8;
    private final Cell[][] cells = new Cell[CHESS_BOARD_SIZE][CHESS_BOARD_SIZE];
    private final Game game;

    public ChessBoard(Game game) {
        this.game = game;
        initializeEmptyBoard();
    }

    private void initializeEmptyBoard() {
        int baseColor;
        for (int y = 0; y < ChessBoard.CHESS_BOARD_SIZE; y++) {
            for (int x = 0; x < ChessBoard.CHESS_BOARD_SIZE; x++) {
                baseColor = ((x+y) % 2 == 0) ? 0 : 1 ;
                this.cells[x][y] = new Cell(baseColor);
            }
        }
    }

    public void clearNotes() {
        for (Cell[] cellList : cells) {
            for (Cell cell : cellList) {
                cell.setSelected(false);
            }
        }
    }

    public void placePieces(Piece piece, int x, int y) {
        this.cells[x][y].setPiece(piece);
        piece.setCell(cells[x][y]);
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }
    public Cell getCell(Position position) {
        return cells[position.getX()][position.getY()];
    }

    public Cell getCellAtRelativePosition(Cell startingCell, Position relativePosition) {
        Position indexes = getIndexOfCell(startingCell);
        if (indexes == null) {
            System.out.println("NULL INDEX");
            return null;
        }
        int returnCellIndexX = indexes.getX() + relativePosition.getX();
        int returnCellIndexY = indexes.getY() + relativePosition.getY();
        if (returnCellIndexX < 0 || CHESS_BOARD_SIZE <= returnCellIndexX || returnCellIndexY < 0 || CHESS_BOARD_SIZE <= returnCellIndexY) {
            return null;
        }
        return cells[returnCellIndexX][returnCellIndexY];
    }

    private Position getIndexOfCell(Cell startingCell) {
        assert startingCell != null;
        for (int x = 0; x < CHESS_BOARD_SIZE; x++) {
            for (int y = 0; y < CHESS_BOARD_SIZE; y++) {
                System.out.println(startingCell.getId() + "   ==?   "+ cells[x][y].getId());
                if (cells[x][y].equals(startingCell)) {
                    System.out.println("AYAYAYA");
                    return new Position(x,y);
                }
            }
        }
        return null;
    }

    public Game getGame() {
        return game;
    }
}
