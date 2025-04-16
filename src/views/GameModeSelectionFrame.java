package views;

import models.Game;
import models.players.CalculatorBotPlayer;
import models.players.HumanPlayer;
import models.players.Player;
import models.players.RandomBotPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameModeSelectionFrame extends JFrame {

    private Game game;
    private boolean needSetUpRotate = false;

    private JPanel contentPane;
    private JPanel tmpPan;
    private JLabel titleLabel;
    private JButton humanDuoBtn;
    private JButton botDuoBtn;
    private JButton botvBotBtn;
    private JButton onlineDuoBtn;
    private JButton humanQuartetBtn;

    public GameModeSelectionFrame() {
        this.build();
    }

    private void build() {

        List<Player> playersTmp = new ArrayList<>();
        playersTmp.add(new HumanPlayer("Bernard", 0));
        playersTmp.add(new HumanPlayer("Bernardette", 1));
        this.game = new Game(playersTmp);

        this.humanDuoBtn     = new JButton(" Mode 1v1 ");
        this.botDuoBtn       = new JButton("Mode 1vBot");
        this.botvBotBtn      = new JButton("Mode BotvBot");
        this.onlineDuoBtn    = new JButton("Mode online");
        this.humanQuartetBtn = new JButton("Mode 1v1v1v1");

        Dimension buttonDim = new Dimension(150, 50);

        this.humanDuoBtn.setPreferredSize(buttonDim);
        this.botDuoBtn.setPreferredSize(buttonDim);
        this.botvBotBtn.setPreferredSize(buttonDim);
        this.onlineDuoBtn.setPreferredSize(buttonDim);
        this.humanQuartetBtn.setPreferredSize(buttonDim);

        this.humanDuoBtn.setMaximumSize(buttonDim);
        this.botDuoBtn.setMaximumSize(buttonDim);
        this.botvBotBtn.setMaximumSize(buttonDim);
        this.onlineDuoBtn.setMaximumSize(buttonDim);
        this.humanQuartetBtn.setMaximumSize(buttonDim);

        this.humanDuoBtn.addActionListener(e -> {
            String firstPlayerName  = JOptionPane.showInputDialog("Nom du 1er joueur");
            String secondPlayerName = JOptionPane.showInputDialog("Nom du 2e joueur");

            List<Player> players = new ArrayList<>();
            players.add(new HumanPlayer(firstPlayerName, 0));
            players.add(new HumanPlayer(secondPlayerName, 1));

            this.needSetUpRotate = true;

            this.game = new Game(players);
            this.startGame();
        });

        this.botDuoBtn.addActionListener(e -> {
            List<Player> players = new ArrayList<>();
            players.add(new HumanPlayer("joueur", 0));
            players.add(new CalculatorBotPlayer(1));

            this.game = new Game(players);
            this.startGame();
        });

        this.botvBotBtn.addActionListener(e -> {
            List<Player> players = new ArrayList<>();
            players.add(new CalculatorBotPlayer(0));
            players.add(new RandomBotPlayer(1));

            this.game = new Game(players);
            this.startGame();
        });

        this.titleLabel = new JLabel("Bienvenu sur un super jeu d'échec, choisissez votre mode de jeu :");

        this.contentPane = new JPanel();
        this.tmpPan      = new JPanel();

        this.tmpPan.setPreferredSize(new Dimension(150, 250));

        this.tmpPan.add(this.humanDuoBtn);
        this.tmpPan.add(this.botDuoBtn);
        this.tmpPan.add(this.botvBotBtn);
        /* here too
        this.tmpPan.add(this.onlineDuoBtn);
        this.tmpPan.add(this.humanQuartetBtn);
         */

        this.contentPane.add(titleLabel);
        this.contentPane.add(this.tmpPan);

        this.setContentPane(this.contentPane);

        this.setTitle("Échecs");
        this.setSize(400, 250);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }

    private void startGame() {
        Thread thread = new Thread(this.game);
        thread.start();

        MainFrame frame = new MainFrame(game);
        if (this.needSetUpRotate) {
            frame.toggleRotating();
        }
        game.addObserver(frame);

        SwingUtilities.invokeLater(() -> frame.setVisible(true));

        this.setVisible(false);
    }
}
