package models.boards;

public record PlayerMove(Cell source, Cell destination) {
    public PlayerMove(PieceMove pieceMove) {
        this(pieceMove.source(), pieceMove.destination());
    }

    public boolean correspondsTo(GameMove gameMove) {
        return (gameMove.moves().getFirst().source().equals(source) && gameMove.moves().getFirst().destination().equals(destination));
    }

    @Override
    public String toString() {
        return "PlayerMove={" + source.toString() + " " + destination.toString() + "}";
    }
}
