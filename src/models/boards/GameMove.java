package models.boards;

import java.util.ArrayList;
import java.util.List;

public record GameMove(List<PieceMove> moves) {
    public GameMove() {
        this(new ArrayList<>());
    }

    public GameMove(PieceMove move) {
        this(new ArrayList<>());
        this.moves.add(move);
    }

    public GameMove(Cell source, Cell destination, Cell captured) {
        this(new PieceMove(source, destination, captured));
    }

    public GameMove(Cell source, Cell destination) {
        this(new PieceMove(source, destination));
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("GameMove={");
        for (PieceMove move : moves) {
            result.append(move.toString()).append(", ");
        }
        result.delete(result.length() - 2, result.length());
        result.append("}");
        return result.toString();
    }
}
