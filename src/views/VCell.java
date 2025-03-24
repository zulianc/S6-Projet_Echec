package views;

import java.awt.*;

public class VCell {
    private int size;
    private final double indexX;
    private final double indexY;

    private final Color baseColor;
    //private Piece piece;
    private boolean isSelected;
    private boolean canMoveOnIt;


    public VCell(Color baseColor, int size, double indexX, double indexY) {
        this.baseColor = baseColor;
        this.isSelected = false;
        this.canMoveOnIt = false;
        this.size = size;
        this.indexX = indexX;
        this.indexY = indexY;
    }

    public void paint(Graphics g) {
        int cellX = (int) indexX * size;
        int cellY = (int) indexY * size;

        Color cellColor = this.isSelected() ? Color.RED : this.getBaseColor();
        g.setColor(cellColor);

        g.fillRect(cellX, cellY, size, size);

        g.setColor(Color.BLACK);
        g.drawRect(cellX, cellY, size, size);
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
