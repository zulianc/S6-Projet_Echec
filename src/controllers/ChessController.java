package controllers;

import models.Cell;
import models.Game;
import models.Move;
import views.MainFrame;
import views.VArrow;
import views.VCell;
import views.VChessBoard;

import java.awt.event.MouseEvent;
import java.util.List;

public class ChessController {
    private final Game gameModel;
    private final MainFrame mainView;
    private Cell before;
    private Cell after;
    private VCell startCell;

    public ChessController(Game gameModel, MainFrame mainView) {
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

    public void controlPressed(VCell vCell) {
        System.out.println("Mouse right pressed");
        this.startCell = vCell;
    }

    public void controlReleased(VCell endCell) {
        if (startCell != null && !startCell.equals(endCell)) {
            this.mainView.getBoard().addArrow(new VArrow(this.startCell, endCell));
            mainView.update();
        }
        startCell = null;
    }

    private void selectFirstCell(VCell vCell) {
        Cell selectedCell = vCell.getCell();
        before = vCell.getCell();
        if (selectedCell.hasPiece() && selectedCell.getPiece().getTeam() == gameModel.getActualPlayer().getTeam()) {
            vCell.setSelected(true);
            List<Cell> cellsToMark = gameModel.getValidCells(selectedCell.getPiece(), gameModel.getActualPlayer());
            System.out.println("cells accessible "+ cellsToMark);
            this.mainView.getBoard().markValidMoveCells(cellsToMark);
        }
    }

    private void selectSecondCell(VCell vCell) {
        after = vCell.getCell();
        gameModel.sendMove(new Move(before, after));
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
        gameModel.sendPromotion(result);
    }

}
