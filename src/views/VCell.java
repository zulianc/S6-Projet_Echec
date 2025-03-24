package views;

import java.awt.*;

public class VCell {
    private final Color baseColor;
    //private Piece piece;
    private boolean isSelected;
    private boolean canMoveOnIt;

    public VCell(Color baseColor) {
        this.baseColor = baseColor;
        this.isSelected = false;
    }

    public Color getBaseColor() {
        return baseColor;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void toggleSelected() {
        isSelected = !isSelected;
    }

    public boolean isCanMoveOnIt() {
        return canMoveOnIt;
    }

    public void setCanMoveOnIt(boolean canMoveOnIt) {
        this.canMoveOnIt = canMoveOnIt;
    }
}
