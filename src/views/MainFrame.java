package views;

import controllers.ChessController;
import models.Game;
import structure.Observer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame extends JFrame implements Observer {
    private JPanel contentPane;
    private VChessBoard board;
    private JLabel label;
    private final Game gameModel;
    private ChessController chessController;

    private void build() {
        this.chessController = new ChessController(gameModel, this);

        this.contentPane = new JPanel();
        this.contentPane.setPreferredSize(new Dimension(800, 600));
        this.contentPane.setLayout(new BorderLayout());

        ArrayList<Color> basesColors = new ArrayList<>();
        basesColors.add(Color.WHITE);
        basesColors.add(Color.BLACK);

        this.board = new VChessBoard(600, gameModel, chessController, basesColors);
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
}
