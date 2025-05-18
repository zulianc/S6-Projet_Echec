package models;

import models.boards.Cell;
import models.boards.GameBoard;
import models.boards.PlayerMove;
import models.games.ChessGame;
import models.games.Game;
import models.pieces.Piece;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PGNConverter {
    public static String convertMoveToPGN(Game game, PlayerMove move) {
        Cell sourceCell = move.source();
        Cell destinationCell = move.destination();

        if (!sourceCell.hasPiece()) return ""; // throw new RuntimeException("move without piece to move ??");

        String coordinates = game.getBoard().getCellCoordinates(destinationCell);
        String piecePieceMoved    = "";
        String pieceCaptured = "";
        String isChecked     = "";
        Piece sourcePiece = sourceCell.getPiece();

        if (((ChessGame) game).isCastling(move)) {
            if (game.getBoard().getDistanceFromMove(move).getX() < 0) {
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
                    piecePieceMoved = game.getBoard().getCellColumn(sourceCell);
                }
            } else {
                piecePieceMoved = sourcePiece.getPieceCode();

                if (destinationCell.hasPiece()) {

                    List<Cell> accessibleCells = new ArrayList<>();
                    for (Piece piece : game.getBoard().getAllPiecesOfTeam(game.getActualPlayer().getTeam())) {
                        if (!game.getBoard().getCellOfPiece(piece).equals(sourceCell) && piece.getPieceName().equals(sourcePiece.getPieceName())) {
                            piece.getPossibleMoves(game).forEach(gameMove -> accessibleCells.add(gameMove.moves().getFirst().destination()));
                        }
                    }
                    for (Cell cell : accessibleCells) {
                        if (cell.equals(destinationCell)) {
                            if (!game.getBoard().getCellColumn(cell).equals(game.getBoard().getCellColumn(sourceCell))) {
                                piecePieceMoved += game.getBoard().getCellColumn(sourceCell);
                            } else {
                                piecePieceMoved += game.getBoard().getCellRow(sourceCell);
                            }
                            break;
                        }
                    }
                }

                if (((ChessGame) game).isNextPlayerInCheckIfMove(move)) {
                    isChecked = "+";
                }

            }
            return piecePieceMoved + pieceCaptured + coordinates + isChecked;
        }
    }

    public static PlayerMove convertPGNToPieceMove(GameBoard GameBoard, String pgn) {
        return null;
    }

    public static String convertGameToPGN(Game game) {
        StringBuilder result = new StringBuilder();

        String white = "[White \""+game.getPlayers().get(0).getName()+"\"]\n";
        String black = "[Black \""+game.getPlayers().get(1).getName()+"\"]\n";
        result.append(white);
        result.append(black);

        result.append("\n");

        Iterator<String> it = game.getMovesNotation().iterator();
        int playNumber = 0;
        int PieceMoveInPlay = game.getPlayerCount();
        int playTemp = PieceMoveInPlay;

        while (it.hasNext()) {
            if (playTemp == PieceMoveInPlay)  {
                playTemp = 0;
                playNumber++;
                result.append(playNumber).append(". ");
            }
            result.append(it.next()).append(" ");
            playTemp++;
        }
        return result.toString();
    }
}
