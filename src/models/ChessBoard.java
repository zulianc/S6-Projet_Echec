package models;

import models.pieces.Piece;
import structure.Position2D;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ChessBoard {
    public static final int CHESS_BOARD_SIZE = 8;
    private final Cell[][] cells = new Cell[CHESS_BOARD_SIZE][CHESS_BOARD_SIZE];
    private final HashMap<Cell, Position2D> cellsPosition = new HashMap<>();

    public ChessBoard() {
        initializeEmptyBoard();
    }

    private void initializeEmptyBoard() {
        for (int y = 0; y < ChessBoard.CHESS_BOARD_SIZE; y++) {
            for (int x = 0; x < ChessBoard.CHESS_BOARD_SIZE; x++) {
                this.cells[x][y] = new Cell();
                this.cellsPosition.put(this.cells[x][y], new Position2D(x, y));
            }
        }
    }

    public void addPiece(Piece piece, int x, int y) {
        this.movePiece(piece, x, y);
    }

    public void addPiece(Piece piece, Cell cell) {
        this.movePiece(piece, cell);
    }

    public void movePiece(Piece piece, Cell cell) {
        cell.setPiece(piece);
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

    public List<Piece> getAllPiecesOfTeam(int team) {
        List<Piece> pieces = new LinkedList<>();

        for (Cell[] cellList : cells) {
            for (Cell cell : cellList) {
                if (cell.getPiece() != null && cell.getPiece().getTeam() == team) {
                    pieces.add(cell.getPiece());
                }
            }
        }

        return pieces;
    }

    public Cell getCellAtRelativePosition(Cell startingCell, Position2D relativePosition) {
        Position2D indexes = getPositionOfCell(startingCell);
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

    public Position2D getPositionOfCell(Cell startingCell) {
        if (startingCell == null) {
            return null;
        }

        return this.cellsPosition.get(startingCell);
    }

    public Cell getCellOfPiece(Piece piece) {
        for (Cell[] cellList : cells) {
            for (Cell cell : cellList) {
                if (cell.getPiece() != null && cell.getPiece().equals(piece)) {
                    return cell;
                }
            }
        }
        return null;
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public Cell getCell(Position2D position) {
        return cells[position.getX()][position.getY()];
    }

    public Cell[][] getCells() {
        return this.cells;
    }
}
