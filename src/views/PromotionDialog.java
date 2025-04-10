package views;


import javax.swing.*;
import java.awt.*;

class PromotionDialog {
    private final JFrame parent;
    private JDialog dialog;
    private String resultPieceName;

    public PromotionDialog(JFrame parent) {
        this.parent = parent;
    }

    private JPanel createEditBox() {
        JPanel panel = new JPanel();

        JLabel dialogTitleLabel = new JLabel("Choose how to promote your pawn");
        panel.add(dialogTitleLabel);
        dialogTitleLabel.setFont(new Font("Arial", Font.BOLD, 20));


        final JTextArea informationText = new JTextArea ();
        informationText.setEditable (true);
        informationText.setLineWrap (true);
        informationText.setWrapStyleWord (true);

        JScrollPane jsp = new JScrollPane (informationText);
        jsp.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jsp.setHorizontalScrollBarPolicy (ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jsp.setPreferredSize (new Dimension(180, 120));
        panel.add (jsp);

        JButton validButton = new JButton ("Valid");
        panel.add(validButton);

        validButton.addActionListener (a -> {
            if (informationText.getText() == null || informationText.getText().isEmpty()) {
                return;
            }

            resultPieceName = informationText.getText();

            dialog.dispose();
        });

        return panel;
    }

    void display () {
        final int DWIDTH  = 200;
        final int DHEIGHT = 240;

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
