package models;

import models.pieces.Piece;

public class Cell {
    private Piece piece;
    private boolean isSelected;
    private boolean canMoveOnIt;
    private final int baseColor;

    public Cell(int baseColor) {

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
}
