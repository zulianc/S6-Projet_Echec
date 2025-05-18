package views;

import controllers.BoardGameController;
import models.PGNConverter;
import models.games.Game;
import models.players.Player;
import structure.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class MainFrame extends JFrame implements Observer {
    private final Game gameModel;
    private BoardGameController boardGameController;

    private VBoard board;
    private JMenuBar menuBar;
    private JPanel contentPane;
    private JLabel titleLabel;
    private JToggleButton toggleRotationMode;
    private JLabel traitLabel;
    private JTextArea pgnTextArea;
    private JLabel pgnLabel;
    private JMenuItem exportMenuItem;
    private JMenuItem infoMenuItem;

    public MainFrame(Game model) {
        this.gameModel = model;
        this.build();
    }

    private void build() {
        this.boardGameController = new BoardGameController(gameModel, this);

        this.contentPane = new JPanel();
        this.contentPane.setPreferredSize(new Dimension(800, 600));
        this.contentPane.setLayout(new BorderLayout());

        this.menuBar = new JMenuBar();

        this.infoMenuItem = new JMenuItem("Info", KeyEvent.VK_I);
        this.infoMenuItem.setPreferredSize(new Dimension(550, 24));
        this.infoMenuItem.addActionListener(e -> JOptionPane.showMessageDialog(null,
                """
                        Ce logiciel a été créé par Melvyn BAUVENT et Julien CHATAIGNER
                        
                        Faites Clique gauche pour jouer et Clique droit pour marquer les cases
                        """
        ));

        this.exportMenuItem = new JMenuItem("Exporter en PGN", KeyEvent.VK_E);
        this.exportMenuItem.setPreferredSize(new Dimension(1, 24));
        this.exportMenuItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showOpenDialog(null);
            File file = null;
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
            }
            try {
                if (file != null) {
                    Files.writeString(file.toPath(), PGNConverter.convertGameToPGN(this.gameModel));
                    JOptionPane.showMessageDialog(null, "Fichier Créé");
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        ArrayList<Color> basesColors = new ArrayList<>();
        basesColors.add(new Color(251, 253, 219));
        basesColors.add(new Color(96, 138, 72));

        this.board = new VBoard(600, gameModel, boardGameController, basesColors);

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

        this.traitLabel = new JLabel("Trait au Blancs");

        this.pgnLabel = new JLabel("PGN :");
        this.pgnTextArea = new JTextArea("");
        this.pgnTextArea.setEditable(false);

        JPanel northPan = new JPanel();
        JPanel westPan  = new JPanel();
        JPanel eastPan  = new JPanel(new BorderLayout());
        JPanel northEastPan = new JPanel(new BorderLayout());
        JPanel centerEastPan = new JPanel(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(
                this.pgnTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        westPan.add(board);
        northPan.add(titleLabel);
        eastPan.add(northEastPan, BorderLayout.NORTH);
        eastPan.add(centerEastPan, BorderLayout.CENTER);
        northEastPan.add(toggleRotationMode, BorderLayout.NORTH);
        northEastPan.add(traitLabel, BorderLayout.CENTER);
        centerEastPan.add(pgnLabel, BorderLayout.NORTH);
        centerEastPan.add(scrollPane, BorderLayout.CENTER);

        northEastPan.setPreferredSize(new Dimension(150, 100));

        this.contentPane.add(westPan,  BorderLayout.WEST);
        this.contentPane.add(northPan, BorderLayout.NORTH);
        this.contentPane.add(eastPan,  BorderLayout.EAST);

        this.setContentPane(this.contentPane);

        setJMenuBar(menuBar);
        menuBar.add(exportMenuItem);
        menuBar.add(infoMenuItem);

        this.setTitle("Échecs");
        this.setSize(772, 710);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }

    public void toggleRotating() {
        this.toggleRotationMode.doClick();
    }

    public Game getGameModel() {
        return gameModel;
    }

    public VBoard getBoard() {
        return board;
    }

    @Override
    public void update() {
        this.board.update();

        String couleur = (this.gameModel.getActualPlayer().getTeam() == 0) ? "Blancs" : "Noirs";
        this.traitLabel.setText("Trait au " + couleur);
        this.pgnTextArea.setText(PGNConverter.convertGameToPGN(this.gameModel));
    }

    @Override
    public void updateParams(Object[] params) {
        if (params instanceof String[]) {
            String signal = (String) params[0];

            switch (signal) {
                case "humanPromotion" -> {
                    PromotionDialog diag = new PromotionDialog(this);
                    diag.display();

                    String result = diag.getResultPieceName();
                    boardGameController.promotionControl(result);
                }
                case "gameEnded" -> {
                    this.board.unmarkValidMoveCells();
                    Player winner = this.gameModel.getActualPlayer();
                    String winnerName = winner.getName();
                    int winnerTeamNum = winner.getTeam();
                    int totalPlayer = this.gameModel.getPlayerCount();
                    String teamWinning = "";
                    if (totalPlayer == 2) {
                        teamWinning = winnerTeamNum == 0 ? "\nLes blancs ont gagnés" : "\nLes noirs ont gagnés";
                    }
                    String winningMessage = "\nVictoire de "+winnerName+teamWinning;

                    String finalMessage;
                    if (this.gameModel.isDraw()) {
                        finalMessage = "Partie finie\nC'est une égalité";
                    } else {
                        finalMessage = "Partie finie" + winningMessage;
                    }

                    JOptionPane.showMessageDialog(this, finalMessage);
                }
                case "unselectAll" -> this.board.unselectAll();
            }
        }
    }
}
