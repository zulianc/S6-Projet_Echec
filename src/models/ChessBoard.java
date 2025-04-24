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
        for (int y = 0; y < ChessBoard.CHESS_BOARD_SIZE; y++) {
            for (int x = 0; x < ChessBoard.CHESS_BOARD_SIZE; x++) {
                this.cells[x][y] = new Cell();
                this.cellsPosition.put(this.cells[x][y], new Position(x, y));
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

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public Cell getCell(Position position) {
        return cells[position.getX()][position.getY()];
    }

    public Cell[][] getCells() {
        return this.cells;
    }

    public Game getGame() {
        return game;
    }

    public String getCellCoordinates(Cell cell) {
        Position pos = getPositionOfCell(cell);
        int y = pos.getY();
        int x = pos.getX();
        int xToChar = x + 97;
        int cordY = -y + 8;

        char xChar = (char) xToChar;

        return String.valueOf(xChar) + cordY;
    }

    public String getCellColumn(Cell cell) {
        Position pos = getPositionOfCell(cell);
        int x = pos.getX();
        int xToChar = x + 97;

        char xChar = (char) xToChar;

        return String.valueOf(xChar);
    }

    public String getCellRow(Cell cell) {
        Position pos = getPositionOfCell(cell);
        int y = pos.getY();
        int cordY = -y + 8;

        return "" + cordY;
    }

    public Position getDistanceFromMove(Move m) {
        Position sourcePosition      = this.getPositionOfCell(m.source());
        Position destinationPosition = this.getPositionOfCell(m.destination());

        return new Position(destinationPosition.getX() - sourcePosition.getX(), destinationPosition.getY() - sourcePosition.getY());
    }

    public boolean isCastling(Move m) {
        return m.source().getPiece().getPieceName().equals("king") && Math.abs(getDistanceFromMove(m).getX()) == 2;
    }
}
