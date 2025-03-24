package views;

import controllers.ChessBoardController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class MainFrame extends JFrame implements Observer {

    private JPanel contentPane;
    private VChessBoard board;
    private JLabel label;

    private void build() {
        this.contentPane = new JPanel();
        this.contentPane.setPreferredSize(new Dimension(800, 600));
        this.contentPane.setLayout(new BorderLayout());

        this.board = new VChessBoard(600);
        this.contentPane.add(board, BorderLayout.CENTER);

        this.label = new JLabel("Super jeu d'échecs");
        this.contentPane.add(label, BorderLayout.NORTH);

        this.setContentPane(this.contentPane);

        this.setTitle("Échecs");
        this.setSize(800, 650);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public MainFrame() {
        this.build();
    }

    @Override
    public void update() {
        board.update();
    }
}
