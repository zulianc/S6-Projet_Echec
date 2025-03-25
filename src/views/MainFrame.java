package views;

import models.ChessBoard;
import structure.Observer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame extends JFrame implements Observer {

    private JPanel contentPane;
    private VChessBoard board;
    private JLabel label;
    private final ChessBoard boardModel;

    private void build() {
        this.contentPane = new JPanel();
        this.contentPane.setPreferredSize(new Dimension(800, 600));
        this.contentPane.setLayout(new BorderLayout());

        ArrayList<Color> basesColors = new ArrayList<>();
        basesColors.add(Color.WHITE);
        basesColors.add(Color.BLACK);

        this.board = new VChessBoard(600, boardModel, basesColors);
        this.contentPane.add(board, BorderLayout.CENTER);

        this.label = new JLabel("Super jeu d'échecs");
        this.contentPane.add(label, BorderLayout.NORTH);

        this.setContentPane(this.contentPane);

        this.setTitle("Échecs");
        this.setSize(800, 650);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }

    public MainFrame(ChessBoard model) {
        this.boardModel = model;
        this.build();
    }

    @Override
    public void update() {
        board.update();
    }
}
