import models.ChessBoard;
import models.Game;
import views.MainFrame;

public class Main {
    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        Game game = new Game(board);
        MainFrame frame = new MainFrame(board);

        board.addObserver(frame);

        frame.setVisible(true);
    }
}
