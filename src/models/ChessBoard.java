package models;

import models.pieces.Piece;
import structure.Position;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ChessBoard {
    public static final int CHESS_BOARD_SIZE = 8;
    private final Cell[][] cells = new Cell[CHESS_BOARD_SIZE][CHESS_BOARD_SIZE];
    private final HashMap<Cell, Position> cellsPosition = new HashMap<>();
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
                this.cellsPosition.put(this.cells[x][y], new Position(x, y));
            }
        }
    }

    public void clearNotes() {
        for (Cell[] cellList : cells) {
            for (Cell cell : cellList) {
                cell.setMarked(false);
            }
        }
    }

    public void addPiece(Piece piece, int x, int y) {
        this.movePiece(piece, x, y);
    }

    public void movePiece(Piece piece, Cell cell) {
        cell.setPiece(piece);

        if (piece != null) {
            piece.setCell(cell);
        }
    }

    public void movePiece(Piece piece, int x, int y) {
        this.movePiece(piece, this.cells[x][y]);
    }

    public List<Piece> getAllPieces() {
        List<Piece> pieces = new LinkedList<>();

        for (Cell[] cellList : cells) {
            for (Cell cell : cellList) {
                if (cell.getPiece() != null) {
                    pieces.add(cell.getPiece());
                }
            }
        }

        return pieces;
    }

    public Cell getCellAtRelativePosition(Cell startingCell, Position relativePosition) {
        Position indexes = getPositionOfCell(startingCell);
        if (indexes == null) {
            return null;
        }
        int returnCellIndexX = indexes.getX() + relativePosition.getX();
        int returnCellIndexY = indexes.getY() + relativePosition.getY();
        if (returnCellIndexX < 0 || CHESS_BOARD_SIZE <= returnCellIndexX || returnCellIndexY < 0 || CHESS_BOARD_SIZE <= returnCellIndexY) {
            return null;
        }
        return cells[returnCellIndexX][returnCellIndexY];
    }

    public Position getPositionOfCell(Cell startingCell) {
        if (startingCell == null) {
            return null;
        }

        return this.cellsPosition.get(startingCell);
    }

    public void markValidMoveCells(List<Cell> cells) {
        for (Cell cell : cells) {
            cell.setCanMoveOnIt(true);
        }
        game.updateAll();
    }

    public void unmarkValidMoveCells() {
        for (int x = 0; x < CHESS_BOARD_SIZE; x++) {
            for (int y = 0; y < CHESS_BOARD_SIZE; y++) {
                this.cells[x][y].setCanMoveOnIt(false);
            }
        }
        game.updateAll();
    }

    public void unselectAll() {
        for (int i = 0; i < CHESS_BOARD_SIZE; i++) {
            for (int j = 0; j < CHESS_BOARD_SIZE; j++) {
                this.cells[i][j].setSelected(false);
            }
        }
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public Cell getCell(Position position) {
        return cells[position.getX()][position.getY()];
    }

    public Game getGame() {
        return game;
    }

}
