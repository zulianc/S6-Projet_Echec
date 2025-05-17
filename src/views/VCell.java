package views;

import models.boards.Cell;
import structure.Position2D;

import java.awt.*;
import java.util.Objects;

public class VCell {
    private static final Color MARKED_COLOR   = Color.RED;
    private static final Color SELECTED_COLOR = Color.BLUE;

    private final Cell cell;

    private boolean isMarked;
    private boolean canMoveOnIt;
    private boolean isSelected;
    private int size;

    private final Color baseColor;


    public VCell(Cell cell, Color baseColor, int size) {
        this.cell = cell;
        this.baseColor = baseColor;
        this.size = size;
        this.isMarked = false;
        this.canMoveOnIt = false;
        this.isSelected  = false;
    }

    public void paint(Graphics g, Position2D position) {
        int cellX = position.getX() * size;
        int cellY = position.getY() * size;

        Color cellColor = this.isMarked   ? MARKED_COLOR   : this.getBaseColor();
        cellColor       = this.isSelected ? SELECTED_COLOR : cellColor;
        g.setColor(cellColor);

        g.fillRect(cellX, cellY, size, size);

        g.setColor(Color.BLACK);
        g.drawRect(cellX, cellY, size, size);

        if (cell.getPiece() != null) {
            Image img = VPiece.getImage(cell.getPiece());
            g.drawImage(img, cellX, cellY, size, size, null);
        }
        if (this.canMoveOnIt){
            g.setColor(Color.LIGHT_GRAY);
            g.fillOval(cellX+(size/4)+1, cellY+(size/4)+1, size/2, size/2);
        }
    }

    public Cell getCell() {
        return cell;
    }

    public Color getBaseColor() {
        return baseColor;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    public void toggleMark() {
        this.isMarked = !this.isMarked;
    }

    public boolean canMoveOnIt() {
        return canMoveOnIt;
    }

    public void setCanMoveOnIt(boolean canMoveOnIt) {
        this.canMoveOnIt = canMoveOnIt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof VCell vCell)) return false;
        return Objects.equals(cell, vCell.cell);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cell);
    }
}
