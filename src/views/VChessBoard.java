package views;

import controllers.ChessController;
import models.Cell;
import models.ChessBoard;
import models.Game;
import structure.Observer;
import structure.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class VChessBoard extends JPanel implements Observer {
    private final int cellSize;
    private List<VCell> vCells;
    private final ChessController controller;
    private final Game model;
    private List<Color> baseColors;

    public VChessBoard(int size, Game model, ChessController controller, List<Color> baseColors) {
        this.model = model;
        this.controller = controller;
        this.cellSize = size / ChessBoard.CHESS_BOARD_SIZE;
        this.setPreferredSize(new Dimension(size, size));

        this.baseColors = new ArrayList<>(baseColors);

        generateCells();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int caseX = (int) ((e.getPoint().getX()-1) / cellSize);
                int caseY = (int) ((e.getPoint().getY()-1) / cellSize);
                int index = caseY * ChessBoard.CHESS_BOARD_SIZE + caseX;

                VCell cellClicked = vCells.get(index);

                controller.control(cellClicked, e);
            }
        });
    }
    @Override
    public void paintComponent(Graphics g) {
        for (VCell currentCell : vCells) {
            currentCell.paint(g);
        }
    }

    private void generateCells() {
        this.vCells = new ArrayList<>();
        Color color;
        for (int y = 0; y < ChessBoard.CHESS_BOARD_SIZE; y++) {
            for (int x = 0; x < ChessBoard.CHESS_BOARD_SIZE; x++) {
                Cell cell = this.model.getBoard().getCell(x, y);
                color = cell.getBaseColor() == 0 ? this.baseColors.get(0) : this.baseColors.get(1) ;
                vCells.add(new VCell(cell, color, cellSize, new Position(x, y)));
            }
        }
    }

    public List<Color> getBaseColors() {
        return baseColors;
    }

    public void setBaseColors(List<Color> baseColors) {
        this.baseColors = baseColors;
    }

    @Override
    public void update() {
        repaint();
    }

    @Override
    public void updateParams(Object[] params) {

    }
}
