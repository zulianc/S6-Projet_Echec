package models;

import models.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class PGNConverter {
    public static String convertMoveToPGN(ChessBoard chessBoard, Move move) {
        Cell sourceCell = move.source();
        Cell destinationCell = move.destination();

        if (!sourceCell.hasPiece()) throw new RuntimeException("move without piece to move ??");

        String coordinates = chessBoard.getCellCoordinates(destinationCell);
        String pieceMoved    = "";
        String pieceCaptured = "";
        String isChecked     = "";
        Piece sourcePiece = sourceCell.getPiece();

        if (chessBoard.isCastling(move)) {
            if (chessBoard.getDistanceFromMove(move).getX() < 0) {
                return "O-O-O";
            } else {
                return "O-O";
            }

        } else {
            if (destinationCell.hasPiece()) {
                pieceCaptured = "x";
            }

            if (sourcePiece.getPieceName().equals("pawn")) {
                if (destinationCell.hasPiece()) {
                    pieceMoved = chessBoard.getCellColumn(sourceCell);
                }
            } else {
                pieceMoved = sourcePiece.getPieceCode();

                if (destinationCell.hasPiece()) {

                    List<Cell> accessibleCells = new ArrayList<>();
                    for (Piece p : chessBoard.getAllPiecesOfTeam(chessBoard.getGame().getActualPlayer().getTeam())) {
                        if (!p.getCell().equals(sourceCell)  && p.getPieceName().equals(sourcePiece.getPieceName())) {
                            accessibleCells.addAll(p.getAccessibleCells(chessBoard));
                        }
                    }
                    for (Cell cell : accessibleCells) {
                        if (cell.equals(destinationCell)) {
                            if (!chessBoard.getCellColumn(cell).equals(chessBoard.getCellColumn(sourceCell))) {
                                pieceMoved += chessBoard.getCellColumn(sourceCell);
                            } else {
                                pieceMoved += chessBoard.getCellRow(sourceCell);
                            }
                            break;
                        }
                    }
                }

                if (chessBoard.getGame().wouldBeInCheckIfMove(move)) {
                    isChecked = "+";
                }

            }
            return pieceMoved + pieceCaptured + coordinates + isChecked;
        }
    }

    public static Move convertPGNToMove(ChessBoard chessBoard, String pgn) {
        return null;
    }
}
