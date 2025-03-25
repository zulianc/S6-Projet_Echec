package controllers;

import models.Cell;
import models.ChessBoard;
import views.VCell;

import java.awt.event.MouseEvent;

public class ChessBoardController {
    private ChessBoard boardModel;

    public ChessBoardController(ChessBoard boardModel) {
        this.boardModel = boardModel;
    }

    public void control(VCell vCell, MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {

            Cell cell = vCell.getCell();
            boardModel.toggleSelect(cell);
        }
    }
}
