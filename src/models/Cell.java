package models;

import java.awt.*;

public class Cell {
    //private Piece piece;
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
}
