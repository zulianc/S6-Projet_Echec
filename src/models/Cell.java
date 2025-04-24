package models;

import models.pieces.Piece;

import java.util.Objects;

public class Cell {
    private static int cellsId = 0;
    private final int id;
    private Piece piece;

    public Cell() {
        this.id = ++cellsId;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public boolean hasPiece() {
        return this.piece != null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return id == cell.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "id=" + id +
                ", piece=" + piece +
                '}';
    }
}
