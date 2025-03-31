import models.ChessBoard;
import models.Game;
import views.MainFrame;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        MainFrame frame = new MainFrame(game);

        game.addObserver(frame);

        frame.setVisible(true);

        game.playGame();
    }
}
