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

            mainView.update();

        }
    }

    public void controlPressed(VCell vCell, MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            startCell = vCell;

        } else if (e.getButton() == MouseEvent.BUTTON1) {
            if (!this.gameModel.hasGameEnded()) {
                if (before == null) {
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

    public void controlReleased(VCell endCell, MouseEvent e) {
        if (startCell != null && !startCell.equals(endCell)) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                this.mainView.getBoard().addArrow(new VArrow(this.startCell, endCell));
                startCell = null;
                mainView.update();
            }
        }
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (!this.gameModel.hasGameEnded()) {
                if (before != null && endCell.canMoveOnIt()) {
                    this.selectSecondCell(endCell);
                }
            }
        }
    }

    private void selectFirstCell(VCell vCell) {
        Cell selectedCell = vCell.getCell();
        before = vCell.getCell();
        if (selectedCell.hasPiece() && selectedCell.getPiece().getTeam() == gameModel.getActualPlayer().getTeam()) {
            vCell.setSelected(true);
            List<Cell> cellsToMark = gameModel.getValidCells(selectedCell.getPiece());
            this.mainView.getBoard().markValidMoveCells(cellsToMark);
        }
    }

    private void selectSecondCell(VCell vCell) {
        after = vCell.getCell();
        gameModel.sendMove(new PlayerMove(before, after));
        this.unselectCells();
    }

    private void unselectCells() {
        this.before = null;
        this.after  = null;
        this.mainView.getBoard().unselectAll();
        this.mainView.getBoard().unmarkValidMoveCells();
        gameModel.updateAll();
    }

    public void promotionControl(String result) {
        ((ChessGame) gameModel).sendPromotion(result);
    }
}
