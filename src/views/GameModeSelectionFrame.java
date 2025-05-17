package views;

import models.games.Chess960Game;
import models.games.ChessGame;
import models.games.Game;
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

    public GameModeSelectionFrame() {
        this.build();
    }

    private void build() {
        List<Player> playersTmp = new ArrayList<>();
        playersTmp.add(new HumanPlayer("Bernard", 0));
        playersTmp.add(new HumanPlayer("Bernardette", 1));
        this.game = new ChessGame(playersTmp);

        JButton humanDuoBtn = new JButton("Classique 1v1");
        JButton botDuoBtn = new JButton("Classique 1vBot");
        JButton botvBotBtn = new JButton("Classique BotvBot");
        JButton onlineDuoBtn = new JButton("Mode online");
        JButton humanQuartetBtn = new JButton("Mode 1v1v1v1");
        JButton humanDuo960Btn = new JButton("960 1v1");
        JButton botDuo960Btn = new JButton("960 1vBot");
        JButton botvBot960Btn = new JButton("960 BotvBot");

        Dimension buttonDim = new Dimension(150, 50);

        humanDuoBtn.setPreferredSize(buttonDim);
        botDuoBtn.setPreferredSize(buttonDim);
        botvBotBtn.setPreferredSize(buttonDim);
        onlineDuoBtn.setPreferredSize(buttonDim);
        humanQuartetBtn.setPreferredSize(buttonDim);
        humanDuo960Btn.setPreferredSize(buttonDim);
        botDuo960Btn.setPreferredSize(buttonDim);
        botvBot960Btn.setPreferredSize(buttonDim);

        humanDuoBtn.setMaximumSize(buttonDim);
        botDuoBtn.setMaximumSize(buttonDim);
        botvBotBtn.setMaximumSize(buttonDim);
        onlineDuoBtn.setMaximumSize(buttonDim);
        humanQuartetBtn.setMaximumSize(buttonDim);
        humanDuo960Btn.setMaximumSize(buttonDim);
        botvBot960Btn.setMaximumSize(buttonDim);
        botvBot960Btn.setMaximumSize(buttonDim);

        humanDuoBtn.addActionListener(e -> {
            String firstPlayerName  = JOptionPane.showInputDialog("Nom du 1er joueur");
            String secondPlayerName = JOptionPane.showInputDialog("Nom du 2e joueur");

            List<Player> players = new ArrayList<>();
            players.add(new HumanPlayer(firstPlayerName, 0));
            players.add(new HumanPlayer(secondPlayerName, 1));

            this.needSetUpRotate = true;

            this.game = new ChessGame(players);
            this.startGame();
        });

        botDuoBtn.addActionListener(e -> {
            List<Player> players = new ArrayList<>();
            players.add(new HumanPlayer("joueur", 0));
            players.add(new CalculatorBotPlayer(1));

            this.game = new ChessGame(players);
            this.startGame();
        });

        botvBotBtn.addActionListener(e -> {
            List<Player> players = new ArrayList<>();
            players.add(new CalculatorBotPlayer(0));
            players.add(new RandomBotPlayer(1));

            this.game = new ChessGame(players);
            this.startGame();
        });

        humanDuo960Btn.addActionListener(e -> {
            String firstPlayerName  = JOptionPane.showInputDialog("Nom du 1er joueur");
            String secondPlayerName = JOptionPane.showInputDialog("Nom du 2e joueur");

            List<Player> players = new ArrayList<>();
            players.add(new HumanPlayer(firstPlayerName, 0));
            players.add(new HumanPlayer(secondPlayerName, 1));

            this.needSetUpRotate = true;

            this.game = new Chess960Game(players);
            this.startGame();
        });

        botDuo960Btn.addActionListener(e -> {
            List<Player> players = new ArrayList<>();
            players.add(new HumanPlayer("joueur", 0));
            players.add(new CalculatorBotPlayer(1));

            this.game = new Chess960Game(players);
            this.startGame();
        });

        botvBot960Btn.addActionListener(e -> {
            List<Player> players = new ArrayList<>();
            players.add(new CalculatorBotPlayer(0));
            players.add(new RandomBotPlayer(1));

            this.game = new Chess960Game(players);
            this.startGame();
        });

        JLabel titleLabel = new JLabel("Bienvenu sur un super jeu d'échec, choisissez votre mode de jeu :");

        JPanel contentPane = new JPanel();
        JPanel tmpPan = new JPanel();

        tmpPan.setPreferredSize(new Dimension(150, 400));

        tmpPan.add(humanDuoBtn);
        tmpPan.add(botDuoBtn);
        tmpPan.add(botvBotBtn);
        /* here too
        this.tmpPan.add(this.onlineDuoBtn);
        this.tmpPan.add(this.humanQuartetBtn);
         */
        tmpPan.add(humanDuo960Btn);
        tmpPan.add(botDuo960Btn);
        tmpPan.add(botvBot960Btn);

        contentPane.add(titleLabel);
        contentPane.add(tmpPan);

        this.setContentPane(contentPane);

        this.setTitle("Échecs");
        this.setSize(400, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }

    private void startGame() {
        Thread thread = new Thread(this.game);
        thread.start();

        MainFrame frame = new MainFrame(this.game);
        if (this.needSetUpRotate) {
            frame.toggleRotating();
        }
        this.game.addObserver(frame);

        SwingUtilities.invokeLater(() -> frame.setVisible(true));

        this.setVisible(false);
    }
}
