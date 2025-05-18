package views;

import models.games.CheckersGame;
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
        JButton humanDuoBtn = new JButton("Echecs 1v1");
        JButton botDuoBtn = new JButton("Echecs 1vBot");
        JButton botvBotBtn = new JButton("Echecs BotvBot");
        JButton onlineDuoBtn = new JButton("Echecs online");
        JButton humanQuartetBtn = new JButton("Echecs 1v1v1v1");
        JButton humanDuo960Btn = new JButton("Echecs 960 1v1");
        JButton botDuo960Btn = new JButton("Echecs 960 1vBot");
        JButton botvBot960Btn = new JButton("Echecs 960 BotvBot");
        JButton checkerHumanDotBtn = new JButton("Dames 1v1");

        List<JButton> buttons = new ArrayList<>();
        buttons.add(humanDuoBtn);
        buttons.add(botDuoBtn);
        buttons.add(botvBotBtn);
        //buttons.add(onlineDuoBtn);
        //buttons.add(humanQuartetBtn);
        buttons.add(humanDuo960Btn);
        buttons.add(botDuo960Btn);
        buttons.add(botvBot960Btn);
        buttons.add(checkerHumanDotBtn);

        Dimension buttonDim = new Dimension(150, 50);

        for (JButton button : buttons) {
            button.setPreferredSize(buttonDim);
            button.setMaximumSize(buttonDim);
        }

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

        checkerHumanDotBtn.addActionListener(e -> {
            String firstPlayerName  = JOptionPane.showInputDialog("Nom du 1er joueur");
            String secondPlayerName = JOptionPane.showInputDialog("Nom du 2e joueur");

            List<Player> players = new ArrayList<>();
            players.add(new HumanPlayer(firstPlayerName, 0));
            players.add(new HumanPlayer(secondPlayerName, 1));

            this.needSetUpRotate = true;

            this.game = new CheckersGame(players);
            this.startGame();
        });

        JLabel titleLabel = new JLabel("Bienvenu sur un super jeu d'échec, choisissez votre mode de jeu :");

        JPanel contentPane = new JPanel();
        JPanel tmpPan = new JPanel();

        tmpPan.setPreferredSize(new Dimension(150, 80 + buttons.size() * 55));

        for (JButton button : buttons) {
            tmpPan.add(button);
        }

        contentPane.add(titleLabel);
        contentPane.add(tmpPan);

        this.setContentPane(contentPane);

        this.setTitle("Échecs");
        this.setSize(400, 80 + buttons.size() * 55);
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
