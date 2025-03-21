package views;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel contentPane;
    private VChessBoard board;
    private JLabel label;

    private void build() {
        this.contentPane = new JPanel();
        this.contentPane.setPreferredSize(new Dimension(800, 600));

        this.board = new VChessBoard(600, 400);
        this.contentPane.add(board);

        /*
        this.label = new JLabel("Super jeu d'échecs");
        this.contentPane.add(label);

         */

        this.setContentPane(this.contentPane);

        this.setTitle("Échecs");
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public MainFrame() {
        this.build();
    }
}
