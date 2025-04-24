package views;

import structure.Position2D;

import java.awt.*;
import java.util.Objects;

public class VArrow {
    private static Color arrowColor = Color.ORANGE;
    private final VCell sourceCell;
    private final VCell targetCell;

    public VArrow(VCell sourceCell, VCell targetCell) {
        this.sourceCell = sourceCell;
        this.targetCell = targetCell;
    }

    public void paint(Graphics g, VChessBoard vBoard) {
        Position2D sourcePosition = vBoard.getVCellPosition(sourceCell);
        Position2D targetPosition = vBoard.getVCellPosition(targetCell);

        int cellSize = vBoard.getCellSize();

        int x1 = sourcePosition.getX() * cellSize + cellSize / 2;
        int y1 = sourcePosition.getY() * cellSize + cellSize / 2;
        int x2 = targetPosition.getX() * cellSize + cellSize / 2;
        int y2 = targetPosition.getY() * cellSize + cellSize / 2;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(arrowColor);
        g2d.setStroke(new BasicStroke(5));

        g2d.drawLine(x1, y1, x2, y2);

        double angle = Math.atan2(y2 - y1, x2 - x1);
        int arrowLength = 10;
        double arrowAngle = Math.toRadians(30);

        int xArrow1 = (int) (x2 - arrowLength * Math.cos(angle - arrowAngle));
        int yArrow1 = (int) (y2 - arrowLength * Math.sin(angle - arrowAngle));
        int xArrow2 = (int) (x2 - arrowLength * Math.cos(angle + arrowAngle));
        int yArrow2 = (int) (y2 - arrowLength * Math.sin(angle + arrowAngle));

        g2d.drawLine(x2, y2, xArrow1, yArrow1);
        g2d.drawLine(x2, y2, xArrow2, yArrow2);
    }

    public static void setArrowColor(Color arrowColor) {
        VArrow.arrowColor = arrowColor;
    }

    @Override
    public String toString() {
        return "VArrow{" +
                "sourceCell=" + sourceCell +
                ", targetCell=" + targetCell +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof VArrow vArrow)) return false;
        return Objects.equals(sourceCell, vArrow.sourceCell) && Objects.equals(targetCell, vArrow.targetCell);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceCell, targetCell);
    }
}
