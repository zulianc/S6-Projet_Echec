package views;

import controllers.ChessBoardController;
import models.Cell;
import models.ChessBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class VChessBoard extends JPanel implements Observer {

    private final int cellSize;
    private List<VCell> vCells;
    private final ChessBoardController controller;
    private final ChessBoard model;
    private List<Color> baseColors;

    public VChessBoard(int size, ChessBoard model, List<Color> baseColors) {
        this.model = model;
        this.controller = new ChessBoardController(model, this);
        this.cellSize = size / ChessBoard.CHESS_BOARD_SIZE;
        this.setPreferredSize(new Dimension(size, size));

        this.baseColors = new ArrayList<>(baseColors);

        generateCells();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("mouse clicked");

                int caseX = (int) ((e.getPoint().getX()-1) / cellSize);
                int caseY = (int) ((e.getPoint().getY()-1) / cellSize);
                System.out.println("caseX: " + caseX + " caseY: " + caseY);
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
                Cell cell = this.model.getCell(x, y);
                color = cell.getBaseColor() == 0 ? this.baseColors.get(0) : this.baseColors.get(1) ;
                vCells.add(new VCell(cell, color, cellSize, x, y));
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
