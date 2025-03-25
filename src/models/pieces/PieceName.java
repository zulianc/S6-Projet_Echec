package models.pieces;

public enum PieceName {
    PIECE(""),
    KING("king"),
    QUEEN("queen"),
    BISHOP("bishop"),
    ROOK("rook"),
    KNIGHT("knight"),
    CHESS_PAWN("pawn");

    private String value;

    PieceName(String value) {}

    public String getValue() {
        return value;
    }
}
