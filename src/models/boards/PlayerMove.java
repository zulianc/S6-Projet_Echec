package models.boards;

public record PlayerMove(Cell source, Cell destination) {
    @Override
    public String toString() {
        return "PieceMove={" + source.toString() + " " + destination.toString() + "}";
    }
}
