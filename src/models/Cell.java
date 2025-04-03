package models;

import models.pieces.Piece;

import java.util.Objects;

public class Cell {
    private static int cellsId = 0;
    private final int id;
    private Piece piece;
    private boolean isSelected;
    private boolean canMoveOnIt;
    private final int baseColor;

    public Cell(int baseColor) {
        this.id = ++cellsId;
        this.isSelected  = false;
        this.canMoveOnIt = false;
        this.baseColor = baseColor;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void toggleSelect() {
        isSelected = !isSelected;
    }

    public boolean isCanMoveOnIt() {
        return canMoveOnIt;
    }

    public void setCanMoveOnIt(boolean canMoveOnIt) {
        this.canMoveOnIt = canMoveOnIt;
    }

    public int getBaseColor() {
        return baseColor;
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

    public int getId() {
        return this.id;
    }
}
