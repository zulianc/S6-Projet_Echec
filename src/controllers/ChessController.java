package controllers;

import models.Cell;
import models.Game;
import models.Move;
import structure.Position;
import views.MainFrame;
import views.VCell;

import java.awt.event.MouseEvent;

public class ChessController {
    private final Game gameModel;
    private final MainFrame mainView;
    private Position before;
    private Position after;

    public ChessController(Game gameModel, MainFrame mainView) {
        this.gameModel = gameModel;
        this.mainView = mainView;

        this.before = null;
        this.after = null;
    }

    public void control(VCell vCell, MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {

            Cell cell = vCell.getCell();
            cell.toggleSelect();

            mainView.update();

        } else if (e.getButton() == MouseEvent.BUTTON1) {
            if (before == null) {
                before = new Position(vCell.getIndexX(), vCell.getIndexY());
            }
            else {
                after = new Position(vCell.getIndexX(), vCell.getIndexY());
                gameModel.sendMove(new Move(before, after));
                this.before = null;
                this.after = null;
            }
        }
    }
}
