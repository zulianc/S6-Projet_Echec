package views;

import models.pieces.chess.Bishop;
import models.pieces.chess.Knight;
import models.pieces.chess.Queen;
import models.pieces.chess.Rook;

import javax.swing.*;
import java.awt.*;

class PromotionDialog {
    private final MainFrame parent;
    private JDialog dialog;
    private String resultPieceName = "queen";
    private final int DWIDTH  = 500;
    private final int DHEIGHT = 240;
    private JToggleButton queenButton;
    private JToggleButton rookButton;
    private JToggleButton bishopButton;
    private JToggleButton knightButton;

    public PromotionDialog(MainFrame parent) {
        this.parent = parent;
    }

    private JPanel createEditBox() {
        int actualTeam = parent.getGameModel().getActualPlayer().getTeam();
        JPanel panel = new JPanel();
        LayoutManager layout = new BorderLayout();
        panel.setLayout(layout);

        JLabel dialogTitleLabel = new JLabel("Choose how to promote your pawn");
        dialogTitleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        queenButton  = new JToggleButton();
        rookButton   = new JToggleButton();
        bishopButton = new JToggleButton();
        knightButton = new JToggleButton();

        ButtonGroup group = new ButtonGroup();
        group.add(queenButton);
        group.add(rookButton);
        group.add(bishopButton);
        group.add(knightButton);

        int imageSize = 72;

        queenButton.setPreferredSize( new Dimension(imageSize, imageSize));
        rookButton.setPreferredSize(  new Dimension(imageSize, imageSize));
        bishopButton.setPreferredSize(new Dimension(imageSize, imageSize));
        knightButton.setPreferredSize(new Dimension(imageSize, imageSize));

        Image queenImage  = VPiece.getImage(new Queen(actualTeam));
        Image rookImage   = VPiece.getImage(new Rook(actualTeam));
        Image bishopImage = VPiece.getImage(new Bishop(actualTeam));
        Image knightImage = VPiece.getImage(new Knight(actualTeam));

        queenButton.setIcon( new ImageIcon(queenImage.getScaledInstance( imageSize, imageSize,  java.awt.Image.SCALE_SMOOTH)));
        rookButton.setIcon(  new ImageIcon(rookImage.getScaledInstance(  imageSize, imageSize,  java.awt.Image.SCALE_SMOOTH)));
        bishopButton.setIcon(new ImageIcon(bishopImage.getScaledInstance(imageSize, imageSize,  java.awt.Image.SCALE_SMOOTH)));
        knightButton.setIcon(new ImageIcon(knightImage.getScaledInstance(imageSize, imageSize,  java.awt.Image.SCALE_SMOOTH)));

        queenButton.addActionListener(e  -> resultPieceName = "queen");
        rookButton.addActionListener(e   -> resultPieceName = "rook");
        bishopButton.addActionListener(e -> resultPieceName = "bishop");
        knightButton.addActionListener(e -> resultPieceName = "knight");

        JButton validButton = new JButton ("Valid");
        validButton.setPreferredSize( new Dimension(imageSize, imageSize/2));
        validButton.addActionListener (a -> dialog.dispose());

        JPanel imagePanel  = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel labelPanel  = new JPanel();

        imagePanel.add(queenButton,  BorderLayout.CENTER);
        imagePanel.add(rookButton,   BorderLayout.CENTER);
        imagePanel.add(bishopButton, BorderLayout.CENTER);
        imagePanel.add(knightButton, BorderLayout.CENTER);
        buttonPanel.add(validButton);
        labelPanel.add(dialogTitleLabel);

        panel.add(imagePanel, BorderLayout.CENTER);
        panel.add(labelPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    void display() {
        dialog = new JDialog(parent, "Promotion", true);
        dialog.setSize(DWIDTH, DHEIGHT);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        dialog.setContentPane(createEditBox());

        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    String getResultPieceName() {
        return resultPieceName;
    }
}
