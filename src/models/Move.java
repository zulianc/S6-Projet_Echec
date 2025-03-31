package models;

import structure.Position;

public class Move {
    private final Position before;
    private final Position after;

    public Move(Position before, Position after) {
        this.before = before;
        this.after = after;
    }

    public Position getBefore() {
        return before;
    }

    public Position getAfter() {
        return after;
    }
}
