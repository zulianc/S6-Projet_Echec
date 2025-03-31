package views;

import models.Cell;
import structure.Position;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VCell {

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

        Color cellColor = this.cell.isSelected() ? Color.RED : this.getBaseColor();
        g.setColor(cellColor);

        g.fillRect(cellX, cellY, size, size);

        g.setColor(Color.BLACK);
        g.drawRect(cellX, cellY, size, size);

        if (cell.getPiece() != null) {
            Image img = VPiece.getImage(cell.getPiece());
            g.drawImage(img, cellX, cellY, size, size, null);
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
