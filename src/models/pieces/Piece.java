package models.pieces;

import models.boards.Cell;
import models.games.Game;
import models.decorators.AccessibleCellsDecorator;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    private final AccessibleCellsDecorator decorator;
    private final int team;
    protected int value;
    private final ArrayList<Integer> moveTurns = new ArrayList<>();

    public Piece(int team, int value, AccessibleCellsDecorator decorator) {
        this.decorator = decorator;
        this.team = team;
        this.value = value;
        this.moveTurns.add(-1);
    }

    public abstract String getPieceName();

    public abstract String getPieceCode();

    public List<Cell> getAccessibleCells(Game game) {
        return decorator.getAccessibleCells(game, this);
    }

    public void signalPieceJustMoved(int turn) {
        this.moveTurns.add(turn);
    }

    public void signalPieceUnmoved() {
        this.moveTurns.removeLast();
    }

    public int getTeam() {
        return team;
    }

    public int getValue() {
        return this.value;
    }

    public int getMoveCount() {
        return this.moveTurns.size() - 1;
    }

    public boolean hasNeverMoved() {
        return this.getMoveCount() == 0;
    }

    public Integer getLastMoveTurn() {
        int lastMoveTurn = this.moveTurns.getLast();

        if (lastMoveTurn == -1) {
            return null;
        }
        return lastMoveTurn;
    }

    @Override
    public String toString() {
        return "Piece{" +
                "name=" + this.getPieceName() +
                ", team=" + team +
                ", value=" + value +
                '}';
    }
}
