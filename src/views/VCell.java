package views;

import models.Cell;
import structure.Position;

import java.awt.*;

public class VCell {

    private static final Color MARKED_COLOR   = Color.RED;
    private static final Color SELECTED_COLOR = Color.BLUE;

    private final Cell cell;

    private int size;
    private final Position coordinates;

    private final Color baseColor;


    public VCell(Cell cell, Color baseColor, int size, Position coordinates) {
        this.cell = cell;
        this.baseColor = baseColor;
        this.size = size;
        this.coordinates = coordinates;
    }

    public void paint(Graphics g) {
        int cellX = coordinates.getX() * size;
        int cellY = coordinates.getY() * size;

        Color cellColor = this.cell.isMarked() ? MARKED_COLOR : this.getBaseColor();
        cellColor = this.cell.isSelected() ? SELECTED_COLOR : cellColor;
        g.setColor(cellColor);

        g.fillRect(cellX, cellY, size, size);

        g.setColor(Color.BLACK);
        g.drawRect(cellX, cellY, size, size);

        if (cell.getPiece() != null) {
            Image img = VPiece.getImage(cell.getPiece());
            g.drawImage(img, cellX, cellY, size, size, null);
        }
        if (cell.canMoveOnIt()){
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

    public int getIndexX() {
        return coordinates.getX();
    }

    public int getIndexY() {
        return coordinates.getY();
    }
}
