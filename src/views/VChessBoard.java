package views;

import javax.swing.*;
import java.awt.*;

public class VChessBoard extends JPanel {
    public static final int CHESS_BOARD_SIZE = 8;

    private int height;
    private int width;

    public VChessBoard(int height, int width) {
        this.height = height;
        this.width = width;
        this.setPreferredSize(new Dimension(width, height));
        this.repaint();
    }
    @Override
    public void paintComponent(Graphics g) {
        this.drawBaseBoard(g);
    }

    private void drawBaseBoard(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        int caseSize = this.getWidth() / CHESS_BOARD_SIZE;
        for (int y = 0; y < CHESS_BOARD_SIZE; y++) {
            for (int x = 0; x < CHESS_BOARD_SIZE; x++) {
                if ((x+y) % 2 == 0) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.fillRect(x*caseSize, y*caseSize, caseSize, caseSize);
            }
        }
    }
}
