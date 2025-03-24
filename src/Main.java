import models.ChessBoard;
import views.MainFrame;

public class Main {
    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        MainFrame frame = new MainFrame();

        board.addObserver(frame);

        frame.setVisible(true);
    }
}
