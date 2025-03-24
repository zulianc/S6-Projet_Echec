package views;

import controllers.ChessBoardController;
import models.ChessBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class VChessBoard extends JPanel implements Observer {

    private final int cellSize;
    private List<VCell> cells;
    private final ChessBoardController controller = new ChessBoardController();

    public VChessBoard(int size) {
        this.cellSize = size / ChessBoard.CHESS_BOARD_SIZE;
        this.setPreferredSize(new Dimension(size, size));

        generateCells();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("mouse clicked");

                int caseX = (int) ((e.getPoint().getX()-1) / cellSize);
                int caseY = (int) ((e.getPoint().getY()-1) / cellSize);
                System.out.println("caseX: " + caseX + " caseY: " + caseY);
                int index = caseY * ChessBoard.CHESS_BOARD_SIZE + caseX;

                VCell cellClicked = cells.get(index);

                if (e.getButton() == MouseEvent.BUTTON3) {

                    cellClicked.toggleSelected();
                    update();
                }
            }
        });
    }
    @Override
    public void paintComponent(Graphics g) {
        for (VCell currentCell : cells) {
            currentCell.paint(g);
        }
    }

    private void generateCells() {
        this.cells = new ArrayList<>();
        Color color;
        for (int y = 0; y < ChessBoard.CHESS_BOARD_SIZE; y++) {
            for (int x = 0; x < ChessBoard.CHESS_BOARD_SIZE; x++) {
                if ((x+y) % 2 == 0) {
                    color = Color.WHITE;
                } else {
                    color = Color.BLACK;
                }
                cells.add(new VCell(color, cellSize, x, y));
            }
        }
    }

    @Override
    public void update() {
        repaint();
    }
}
