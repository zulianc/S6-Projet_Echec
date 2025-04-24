package models.pieces;

import models.Cell;
import models.Game;
import models.decorators.AccessibleCellsDecorator;

import java.util.List;

public abstract class Piece {
    private final AccessibleCellsDecorator decorator;
    private final int team;
    protected int value;
    private int moveCount;
    private int lastMoveTurn;

    public Piece(int team, int value, AccessibleCellsDecorator decorator) {
        this.decorator = decorator;
        this.team = team;
        this.value = value;
        this.moveCount = 0;
        this.lastMoveTurn = -1;
    }

    public abstract String getPieceName();

    public List<Cell> getAccessibleCells(Game game) {
        return decorator.getAccessibleCells(game, this);
    }

    public void signalPieceJustMoved(int turn) {
        this.moveCount++;
        this.lastMoveTurn = turn;
    }

    public int getTeam() {
        return team;
    }

    public int getValue() {
        return this.value;
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

    @Override
    public String toString() {
        return "Piece{" +
                "team=" + team +
                ", value=" + value +
                '}';
    }
}
