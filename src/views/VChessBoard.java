package views;

import javax.swing.*;
import java.awt.*;

public class VChessBoard extends JPanel {
    public static final int CHESS_BOARD_SIZE = 8;

    private int size;

    public VChessBoard(int size) {
        this.size = size;
        this.setPreferredSize(new Dimension(size, size));
    }
    @Override
    public void paintComponent(Graphics g) {
        this.drawBaseBoard(g);
    }

    private void drawBaseBoard(Graphics g) {

        int caseSize = this.size / CHESS_BOARD_SIZE;
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
