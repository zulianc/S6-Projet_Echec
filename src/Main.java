import models.ChessBoard;
import models.Game;
import models.Player;
import views.MainFrame;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Bernard"));
        players.add(new Player("Bernardette"));

        Game game = new Game(players);
        MainFrame frame = new MainFrame(game);

        game.addObserver(frame);

        frame.setVisible(true);

        Thread thread = new Thread(game);
        thread.start();
    }
}
