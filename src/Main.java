import views.GameModeSelectionFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        GameModeSelectionFrame frame = new GameModeSelectionFrame();
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }
}
