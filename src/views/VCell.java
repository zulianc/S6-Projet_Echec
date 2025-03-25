package views;

import models.Cell;

import java.awt.*;

public class VCell {

    private final Cell cell;

    private int size;
    private final int indexX;
    private final int indexY;

    private final Color baseColor;


    public VCell(Cell cell, Color baseColor, int size, int indexX, int indexY) {
        this.cell = cell;
        this.baseColor = baseColor;
        this.size = size;
        this.indexX = indexX;
        this.indexY = indexY;
    }

    public void paint(Graphics g) {
        int cellX = indexX * size;
        int cellY = indexY * size;

        Color cellColor = this.cell.isSelected() ? Color.RED : this.getBaseColor();
        g.setColor(cellColor);

        g.fillRect(cellX, cellY, size, size);

        g.setColor(Color.BLACK);
        g.drawRect(cellX, cellY, size, size);
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


}
