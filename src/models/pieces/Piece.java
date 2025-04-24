package models.pieces;

import models.Cell;
import models.ChessBoard;
import models.Game;
import models.decorators.AccessibleCellsDecorator;

import java.util.List;

public abstract class Piece {
    private final int team;
    private final AccessibleCellsDecorator decorator;
    private int moveCount;
    private int lastMoveTurn;
    protected int value;

    public Piece(int team, AccessibleCellsDecorator decorator, int value) {
        this.team = team;
        this.decorator = decorator;
        this.value = value;
        this.lastMoveTurn = -1;
        this.moveCount = 0;
    }

    public abstract String getPieceName();

    public void signalPieceJustMoved(int turn) {
        this.moveCount++;
        this.lastMoveTurn = turn;
    }

    public int getTeam() {
        return team;
    }

    public List<Cell> getAccessibleCells(Game game) {
        return decorator.getAccessibleCells(game, this);
    }

    public int getMoveCount() {
        return this.moveCount;
    }

    public boolean hasNeverMoved() {
        return this.moveCount == 0;
    }

    public Integer getLastMoveTurn() {
        if (this.lastMoveTurn == -1) {
            return null;
        }
        return this.lastMoveTurn;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "Piece{" +
                "team=" + team +
                "value=" + value +
                '}';
    }
}
