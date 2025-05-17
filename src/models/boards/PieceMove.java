package models.boards;

public record PieceMove(Cell source, Cell destination, Cell captured) {
    public PieceMove(Cell source, Cell destination) {
        this(source, destination, destination);
    }

    @Override
    public String toString() {
        return "PieceMove={" + source.toString() + " " + destination.toString() + " " + captured.toString() + "}";
    }
}
