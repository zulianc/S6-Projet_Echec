package views;

import controllers.BoardGameController;
import models.boards.Cell;
import models.games.Game;
import structure.Observer;
import structure.Position2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VBoard extends JPanel implements Observer {
    private final int cellSize;
    private List<VCell> vCells;
    private final List<VArrow> vArrows = new LinkedList<>();
    private final Game model;
    private List<Color> baseColors;
    private boolean isRotating = false;
    private int currentRotationDegree = 0;

    public VBoard(int size, Game model, BoardGameController controller, List<Color> baseColors) {
        this.model = model;
        this.cellSize = size / this.model.getBoard().getBoardSize();
        this.setPreferredSize(new Dimension(size, size));

        this.baseColors = new ArrayList<>(baseColors);

        generateCells();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                VCell cellClicked = collectVCellFromEvent(e);
                controller.controlClicked(cellClicked, e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                VCell cellEnd = collectVCellFromEvent(e);
                controller.controlPressed(cellEnd, e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                VCell cellEnd = collectVCellFromEvent(e);
                controller.controlReleased(cellEnd, e);
            }
        });
    }

    private VCell collectVCellFromEvent(MouseEvent e) {
        int caseX = (int) ((e.getPoint().getX() - 1) / cellSize);
        int caseY = (int) ((e.getPoint().getY() - 1) / cellSize);
        int index = caseY * this.model.getBoard().getBoardSize() + caseX;

        return vCells.get(index);
    }

    private void generateCells() {
        this.vCells = new ArrayList<>();
        Color color;
        for (int y = 0; y < this.model.getBoard().getBoardSize(); y++) {
            for (int x = 0; x < this.model.getBoard().getBoardSize(); x++) {
                Cell cell = this.model.getBoard().getCell(x, y);
                color = ((x+y) % 2 == 0) ? this.baseColors.get(0) : this.baseColors.get(1) ;
                vCells.add(new VCell(cell, color, cellSize));
            }
        }
    }

    private void tryToRotate() {
        double actualPlayerTeam = model.getActualPlayer().getTeam();
        int playerCount = model.getPlayerCount();
        int rotationDegree = (int)(actualPlayerTeam/playerCount * 360);
        if (rotationDegree != this.currentRotationDegree) {
            vCells = new ArrayList<>(vCells.reversed());
            this.currentRotationDegree = rotationDegree;
        }
    }

    public void markValidMoveCells(List<Cell> cellsToMark) {
        vCells.stream()
                .filter(vCell -> cellsToMark.contains(vCell.getCell()))
                .forEach(vCellToMark -> vCellToMark.setCanMoveOnIt(true));
        update();
    }

    public void unmarkValidMoveCells() {
        for (VCell vCell : vCells) {
            vCell.setCanMoveOnIt(false);
        }
        update();
    }

    public void unselectAll() {
        for (VCell vCell : vCells) {
            vCell.setSelected(false);
        }
    }

    public void clearNotes() {
        for (VCell vCell : vCells) {
            vCell.setMarked(false);
        }
        this.vArrows.clear();
    }

    public void setBaseColors(List<Color> baseColors) {
        this.baseColors = baseColors;
    }

    public Position2D getVCellPosition(VCell vCell) {
        int position = vCells.indexOf(vCell);
        return Position2D.getPositionFromIndex(position, this.model.getBoard().getBoardSize());
    }

    public void setRotating(boolean isRotating) {
        this.isRotating = isRotating;
    }

    public void toggleRotating() {
        isRotating = !isRotating;
        tryToRotate();
        update();
    }

    public void addArrow(VArrow vArrow) {
        if (!this.vArrows.contains(vArrow)) {
            this.vArrows.add(vArrow);
        }
    }

    public int getCellSize() {
        return cellSize;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (isRotating) {
            tryToRotate();
        }
        for (VCell vCell : vCells) {
            vCell.paint(g, this.getVCellPosition(vCell));
        }
        if (!vArrows.isEmpty()) {
            for (VArrow vArrow : vArrows) {
                vArrow.paint(g, this);
            }
        }
    }

    @Override
    public void update() {
        repaint();
    }

    @Override
    public void updateParams(Object[] params) {

    }
}
