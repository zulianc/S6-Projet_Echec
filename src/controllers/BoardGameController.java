package controllers;

import models.boards.Cell;
import models.games.ChessGame;
import models.games.Game;
import models.boards.PlayerMove;
import views.MainFrame;
import views.VArrow;
import views.VCell;

import java.awt.event.MouseEvent;
import java.util.List;

public class BoardGameController {
    private final Game gameModel;
    private final MainFrame mainView;
    private Cell before;
    private Cell after;
    private VCell startCell;

    public BoardGameController(Game gameModel, MainFrame mainView) {
        this.gameModel = gameModel;
        this.mainView  = mainView;

        this.before = null;
        this.after  = null;
    }

    public void controlClicked(VCell vCell, MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {

            if (e.getClickCount() != 2) {
                vCell.toggleMark();
            } else {
                this.mainView.getBoard().clearNotes();
            }

            this.mainView.update();
        }
    }

    public void controlPressed(VCell vCell, MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            this.startCell = vCell;
        }
    }

    public void controlReleased(VCell vCell, MouseEvent e) {
        if (this.startCell != null && !this.startCell.equals(vCell)) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                this.mainView.getBoard().addArrow(new VArrow(this.startCell, vCell));
                this.startCell = null;
                this.mainView.update();
            }
        }

        if (e.getButton() == MouseEvent.BUTTON1) {
            if (!this.gameModel.hasGameEnded()) {
                if (this.before == null) {
                    this.selectFirstCell(vCell);
                } else {
                    if (vCell.canMoveOnIt()) {
                        this.selectSecondCell(vCell);
                    } else {
                        this.unselectCells();
                        this.selectFirstCell(vCell);
                    }
                }
            }
        }
    }

    private void selectFirstCell(VCell vCell) {
        Cell selectedCell = vCell.getCell();
        this.before = vCell.getCell();
        if (selectedCell.hasPiece() && selectedCell.getPiece().getTeam() == this.gameModel.getActualPlayer().getTeam()) {
            vCell.setSelected(true);
            List<Cell> cellsToMark = this.gameModel.getValidCells(selectedCell.getPiece());
            this.mainView.getBoard().markValidMoveCells(cellsToMark);
        }
    }

    private void selectSecondCell(VCell vCell) {
        this.after = vCell.getCell();
        this.gameModel.sendMove(new PlayerMove(this.before, this.after));
        this.unselectCells();
    }

    private void unselectCells() {
        this.before = null;
        this.after  = null;
        this.mainView.getBoard().unselectAll();
        this.mainView.getBoard().unmarkValidMoveCells();
        this.gameModel.updateAll();
    }

    public void promotionControl(String result) {
        ((ChessGame) this.gameModel).sendPromotion(result);
    }
}
