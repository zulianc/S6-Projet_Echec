package controllers;

import models.Cell;
import models.ChessBoard;
import views.VCell;
import views.VChessBoard;

import java.awt.event.MouseEvent;

public class ChessBoardController {
    private final ChessBoard boardModel;
    private final VChessBoard boardView;

    public ChessBoardController(ChessBoard boardModel, VChessBoard boardView) {
        this.boardModel = boardModel;
        this.boardView = boardView;
    }

    public void control(VCell vCell, MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {

            Cell cell = vCell.getCell();
            cell.toggleSelect();

            boardView.update();
        }
    }
}
