package models.boards;

import models.pieces.Piece;
import structure.Position2D;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GameBoard {
    public final int boardSize;
    private final Cell[][] cells;
    private final HashMap<Cell, Position2D> cellsPosition = new HashMap<>();
    private final HashMap<Piece, Cell> piecesCells = new HashMap<>();

    public GameBoard(int boardSize) {
        this.boardSize = boardSize;
        cells = new Cell[boardSize][boardSize];
        initializeEmptyBoard();
    }

    private void initializeEmptyBoard() {
        for (int y = 0; y < this.boardSize; y++) {
            for (int x = 0; x < this.boardSize; x++) {
                this.cells[x][y] = new Cell();
                this.cellsPosition.put(this.cells[x][y], new Position2D(x, y));
            }
        }
    }

    public void setPieceToCell(Piece piece, Cell cell) {
        if (piece != null && !cell.hasPiece()) {
            this.piecesCells.put(piece, cell);
        }

        cell.setPiece(piece);
    }

    public void setPieceToCell(Piece piece, int x, int y) {
        this.setPieceToCell(piece, this.cells[x][y]);
    }

    public void removePieceFromBoard(Piece piece) {
        if (piece != null) {
            this.piecesCells.get(piece).setPiece(null);
            this.piecesCells.remove(piece);
        }
    }

    public List<Piece> getAllPieces() {
        return new LinkedList<>(this.piecesCells.keySet());
    }

    public List<Piece> getAllPiecesOfTeam(int team) {
        List<Piece> pieces = new LinkedList<>();

        for (Piece piece : this.piecesCells.keySet()) {
            if (piece.getTeam() == team) {
                pieces.add(piece);
            }
        }

        return pieces;
    }

    public Cell getCellAtRelativePosition(Cell startingCell, Position2D relativePosition) {
        Position2D indexes = this.getPositionOfCell(startingCell);
        if (indexes == null) {
            return null;
        }
        int returnCellIndexX = indexes.getX() + relativePosition.getX();
        int returnCellIndexY = indexes.getY() + relativePosition.getY();
        if (returnCellIndexX < 0 || this.boardSize <= returnCellIndexX || returnCellIndexY < 0 || this.boardSize <= returnCellIndexY) {
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
        if (piece == null) {
            return null;
        }

        return this.piecesCells.get(piece);
    }

    public Position2D getPositionOfPiece(Piece piece) {
        if (piece == null) {
            return null;
        }

        return this.cellsPosition.get(this.piecesCells.get(piece));
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

    public int getBoardSize() {
        return this.boardSize;
    }

    public String getCellCoordinates(Cell cell) {
        Position2D pos = getPositionOfCell(cell);
        int y = pos.getY();
        int x = pos.getX();
        int xToChar = x + 97;
        int cordY = -y + 8;

        char xChar = (char) xToChar;

        return String.valueOf(xChar) + cordY;
    }

    public String getCellColumn(Cell cell) {
        Position2D pos = getPositionOfCell(cell);
        int x = pos.getX();
        int xToChar = x + 97;

        char xChar = (char) xToChar;

        return String.valueOf(xChar);
    }

    public String getCellRow(Cell cell) {
        Position2D pos = getPositionOfCell(cell);
        int y = pos.getY();
        int cordY = -y + 8;

        return "" + cordY;
    }

    public Position2D getDistanceFromMove(PlayerMove m) {
        Position2D sourcePosition      = this.getPositionOfCell(m.source());
        Position2D destinationPosition = this.getPositionOfCell(m.destination());

        return new Position2D(destinationPosition.getX() - sourcePosition.getX(), destinationPosition.getY() - sourcePosition.getY());
    }

    public Cell getCellFromCoords(String destCord) {
        int x = destCord.charAt(0) - 97;
        int y = Integer.parseInt(String.valueOf(destCord.charAt(1)));

        return getCell(x, y);
    }
}
