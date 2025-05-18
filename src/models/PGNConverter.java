package models;

import models.boards.*;
import models.games.ChessGame;
import models.games.Game;
import models.pieces.Piece;
import models.pieces.chess.King;
import structure.Position2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static List<String> tokenizePGN(String pgn) {
        // On supprime les en-têtes [Tag "…"]
        String movesSection = pgn.replaceAll("\\[.*?\\]\\s*", "").trim();
        // On enlève le résultat en fin (1-0, 0-1, 1/2-1/2)
        movesSection = movesSection.replaceAll("(1-0|0-1|1/2-1/2)\\s*$", "");
        // On split sur les espaces en retirant les numéros de coup (ex. "1.", "2.")
        String[] parts = movesSection.split("\\s+");
        List<String> tokens = new ArrayList<>();
        for (String p : parts) {
            if (p.matches("\\d+\\.")) continue;
            tokens.add(p);
        }
        return tokens;
    }


    public static PlayerMove convertPGNToPieceMove(Game game, String pgn) {
        GameBoard board = game.getBoard();
        if (pgn.equals("O-O") || pgn.equals("O-O-O")) {
            return handleCastling(game, pgn);
        }

        String clean = pgn.replaceAll("[+#]", "")
                .replaceAll("=[QRNBqknrb]", "");

        Pattern pattern = Pattern.compile("^([KQRNB]?)([a-h]?[1-8]?)(x?)([a-h][1-8])$");
        Matcher m = pattern.matcher(clean);
        if (!m.matches()) return null;

        String pieceCode = m.group(1);
        String disamb = m.group(2);
        String destCoord = m.group(4);

        Cell destination = board.getCellFromCoords(destCoord);
        List<Piece> candidates = new ArrayList<>();

        for (Piece pc : board.getAllPiecesOfTeam(game.getActualPlayer().getTeam())) {
            String code = pc.getPieceCode();
            if (pieceCode.isEmpty()) {
                if (!pc.getPieceName().equals("pawn")) continue;
            } else {
                if (!code.equals(pieceCode)) continue;
            }
            for (GameMove gm : pc.getPossibleMoves(game)) {
                PieceMove pm = gm.moves().getFirst();
                if (pm.destination().equals(destination)) {
                    candidates.add(pc);
                    break;
                }
            }
        }

        if (disamb.length() == 1) {
            String s = String.valueOf(disamb.charAt(0));
            candidates.removeIf(pc -> {
                Cell src = board.getCellOfPiece(pc);
                return !(board.getCellColumn(src).equals(s) || board.getCellRow(src).equals(s));
            });
        }

        if (candidates.size() != 1) {
            throw new IllegalStateException("Error PGN " + pgn + " : " + candidates);
        }

        Piece chosen = candidates.getFirst();
        Cell source = board.getCellOfPiece(chosen);

        return new PlayerMove(source, destination);
    }

    private static PlayerMove handleCastling(Game game, String pgn) {
        GameBoard board = game.getBoard();
        PlayerMove kingMove;
        Cell kingSource = null;
        for(Piece piece : board.getAllPiecesOfTeam(game.getActualPlayer().getTeam())) {
            if (piece instanceof King) {
                kingSource = board.getCellOfPiece(piece);
            }
        }
        Position2D kingCoords = board.getPositionOfCell(kingSource);
        int dir = pgn.equals("O-O") ? +2 : -2;
        Cell kingDest = board.getCell(
                kingCoords.getX() + dir,
                kingCoords.getY()
        );
        kingMove = new PlayerMove(kingSource, kingDest);
        return kingMove;
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

    public static List<PlayerMove> convertGameFromPGN(Game game, String pgn) {
        List<PlayerMove> result = new ArrayList<>();
        List<String> moves = tokenizePGN(pgn);

        for (String notation : moves) {
            PlayerMove move = convertPGNToPieceMove(game, notation);
            if (move == null) {
                throw new IllegalArgumentException("Impossible d’interpréter le coup PGN: " + notation);
            }
            result.add(move);

            game.applyMove(move);
            game.nextPlayer();
        }
        return result;
    }
}
