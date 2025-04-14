package views;

import controllers.ChessController;
import models.Game;
import structure.Observer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame extends JFrame implements Observer {
    private final Game gameModel;
    private ChessController chessController;

    private VChessBoard board;
    private JPanel contentPane;
    private JLabel label;
    private JToggleButton toggleRotationMode;


    private void build() {
        this.chessController = new ChessController(gameModel, this);

        this.contentPane = new JPanel();
        this.contentPane.setPreferredSize(new Dimension(800, 600));
        this.contentPane.setLayout(new BorderLayout());

        ArrayList<Color> basesColors = new ArrayList<>();
        basesColors.add(Color.WHITE);
        basesColors.add(Color.BLACK);

        this.board = new VChessBoard(600, gameModel, chessController, basesColors);

        this.label = new JLabel("Super jeu d'échecs");

        this.toggleRotationMode = new JToggleButton("No Rotation");
        this.toggleRotationMode.addActionListener(e -> {
            if (toggleRotationMode.isSelected()) {
                toggleRotationMode.setSelected(false);
                toggleRotationMode.setText("No Rotation");
                this.board.toggleRotating();
            } else {
                toggleRotationMode.setSelected(true);
                toggleRotationMode.setText("Rotation each turn");
                this.board.toggleRotating();
            }
        });

        this.contentPane.add(board, BorderLayout.CENTER);
        this.contentPane.add(label, BorderLayout.NORTH);
        this.contentPane.add(toggleRotationMode, BorderLayout.EAST);

        this.setContentPane(this.contentPane);

        this.setTitle("Échecs");
        this.setSize(800, 650);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }

    public MainFrame(Game model) {
        this.gameModel = model;
        this.build();
    }

    @Override
    public void update() {
        board.update();
    }

    @Override
    public void updateParams(Object[] params) {
        if (params instanceof String[]) {
            String signal = (String) params[0];

            if (signal.equals("promotion")) {
                PromotionDialog diag = new PromotionDialog(this);
                diag.display();

                String result = diag.getResultPieceName();
                chessController.promotionControl(result);

            } else if (signal.equals("gameEnded")) {
                JOptionPane.showMessageDialog(this, "Partie fini");
            }
        }
    }

    public Game getGameModel() {
        return gameModel;
    }

    public VChessBoard getVBoard() {
        return this.board;
    }
}
