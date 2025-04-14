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
    private JLabel titleLabel;
    private JToggleButton toggleRotationMode;

    public MainFrame(Game model) {
        this.gameModel = model;
        this.build();
    }

    private void build() {
        this.chessController = new ChessController(gameModel, this);

        this.contentPane = new JPanel();
        this.contentPane.setPreferredSize(new Dimension(800, 600));
        this.contentPane.setLayout(new BorderLayout());

        ArrayList<Color> basesColors = new ArrayList<>();
        basesColors.add(Color.WHITE);
        basesColors.add(Color.BLACK);

        this.board = new VChessBoard(600, gameModel, chessController, basesColors);

        this.titleLabel = new JLabel("Super jeu d'échecs");
        this.titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        this.toggleRotationMode = new JToggleButton("No Rotation", false);
        this.toggleRotationMode.addActionListener(e -> {
            if (toggleRotationMode.isSelected()) {
                toggleRotationMode.setText("Rotation each turn");
                this.board.toggleRotating();
            } else {
                toggleRotationMode.setText("No Rotation");
                this.board.toggleRotating();
            }
        });

        JPanel northPan = new JPanel();
        JPanel westPan  = new JPanel();
        JPanel eastPan  = new JPanel();

        westPan.add(board);
        northPan.add(titleLabel);
        eastPan.add(toggleRotationMode);

        this.contentPane.add(westPan,  BorderLayout.WEST);
        this.contentPane.add(northPan, BorderLayout.NORTH);
        this.contentPane.add(eastPan,  BorderLayout.EAST);

        this.setContentPane(this.contentPane);

        this.setTitle("Échecs");
        this.setSize(772, 690);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }

    public Game getGameModel() {
        return gameModel;
    }

    public VChessBoard getBoard() {
        return board;
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
                this.board.unmarkValidMoveCells();
                JOptionPane.showMessageDialog(this, "Partie fini");

            } else if (signal.equals("unselectAll")) {
                this.board.unselectAll();
            }
        }
    }
}
