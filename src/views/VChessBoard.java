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

    private int size;
    private int caseSize;
    private List<VCell> cells;
    private final ChessBoardController controller = new ChessBoardController();

    public VChessBoard(int size) {
        this.size = size;
        this.setPreferredSize(new Dimension(size, size));

        generateCells();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("mouse clicked");

                int caseX = (int) ((e.getPoint().getX()-1) / caseSize);
                int caseY = (int) ((e.getPoint().getY()-1) / caseSize);
                System.out.println("caseX: " + caseX + " caseY: " + caseY);
                int index = caseY * ChessBoard.CHESS_BOARD_SIZE + caseX;

                VCell cellClicked = cells.get(index);

                if (e.getButton() == MouseEvent.BUTTON3) {

                    cellClicked.toggleSelected();
                }
            }
        });
    }
    @Override
    public void paintComponent(Graphics g) {
        this.drawBoard(g);
    }

    private void drawBoard(Graphics g) {

        g.setColor(Color.WHITE);
        this.caseSize = this.size / ChessBoard.CHESS_BOARD_SIZE;
        for (int y = 0; y < ChessBoard.CHESS_BOARD_SIZE; y++) {
            for (int x = 0; x < ChessBoard.CHESS_BOARD_SIZE; x++) {
                int index = y*ChessBoard.CHESS_BOARD_SIZE + x;
                int caseX = x*caseSize;
                int caseY = y*caseSize;

                VCell currentCell = cells.get(index);

                Color cellColor = currentCell.isSelected() ? Color.RED : currentCell.getBaseColor();
                g.setColor(cellColor);

                g.fillRect(caseX, caseY, caseSize, caseSize);

                g.setColor(Color.BLACK);
                g.drawRect(caseX, caseY, caseSize, caseSize);
            }
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
                cells.add(new VCell(color));
            }
        }
    }

    @Override
    public void update() {
        repaint();
    }
}
