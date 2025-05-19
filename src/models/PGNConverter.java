package models;

import models.boards.*;
import models.games.ChessGame;
import models.games.Game;
import models.pieces.Piece;
import models.pieces.chess.King;
import models.players.HumanPlayer;
import models.players.Player;
import structure.Position2D;
import views.MainFrame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PGNConverter {

    private static String promotionName = "";

    public static String convertMoveToPGN(Game game, PieceMove move) {
        Cell sourceCell = move.source();
        Cell destinationCell = move.destination();

        if (!sourceCell.hasPiece()) throw new RuntimeException("move without piece to move ??");

        String coordinates = game.getBoard().getCellCoordinates(destinationCell);
        String piecePieceMoved = "";
        String pieceCaptured = "";
        String isChecked     = "";
        String promotion     = "";
        Piece sourcePiece = sourceCell.getPiece();

        if (((ChessGame) game).isCastling(new PlayerMove(move))) {
            if (game.getBoard().getDistanceFromMove(new PlayerMove(move)).getX() < 0) {
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

                if (((ChessGame) game).isNextPlayerInCheckIfMove(new PlayerMove(move))) {
                    isChecked = "+";
                }

            }
            return piecePieceMoved + pieceCaptured + coordinates + promotion + isChecked;
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

    public static List<PlayerMove> convertPGNToPieceMove(Game game, String pgn) {
        GameBoard board = game.getBoard();
        ArrayList<PlayerMove> playerMoves = new ArrayList<>();
        if (pgn.equals("O-O") || pgn.equals("O-O-O")) {
            playerMoves.add(handleCastling(game, pgn));
            return playerMoves;
        }

        String clean = pgn.replaceAll("[+#]", "");

        Pattern pattern = Pattern.compile("^([KQRNB]?)([a-h]?[1-8]?)(x?)([a-h][1-8])((=[QRNBqknrb])?)$");
        Matcher m = pattern.matcher(clean);
        if (!m.matches()) return null;

        String pieceCode = m.group(1);
        String disamb    = m.group(2);
        String destCoord = m.group(4);
        String promotionCode = m.group(5).replaceAll("=", "");
        PGNConverter.promotionName = switch (promotionCode) {
            case "R" -> "rook";
            case "B" -> "bishop";
            case "N" -> "knight";
            default -> "queen";
        };

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

        playerMoves.add(new PlayerMove(source, destination));

        if (!promotionCode.isEmpty()) {
            playerMoves.add(new PlayerMove(null, null));
        }

        return playerMoves;
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

        String playerName1 = game.getPlayers().get(0).getName() != null ? game.getPlayers().get(0).getName() : "?";
        String playerName2 = game.getPlayers().get(1).getName() != null ? game.getPlayers().get(1).getName() : "?";

        result.append("[White \"").append(playerName1).append("\"]\n");
        result.append("[Black \"").append(playerName2).append("\"]\n");

        List<String> moves = game.getMovesNotation();
        int playNumber = 0;
        int movesPerTurn = game.getPlayerCount();
        int turnCounter = movesPerTurn;

        for (int i = 0; i < moves.size(); i++) {
            if (turnCounter == movesPerTurn) {
                turnCounter = 0;
                playNumber++;
                result.append("\n")
                        .append(playNumber).append(". ");
            }
            result.append(moves.get(i)).append(" ");
            turnCounter++;
        }

        return result.toString();
    }

    public static void createGameFromPGN(String pgn) {
        List<String> playerNames = PGNConverter.getPlayersFromPGN(pgn);

        List<Player> players = new ArrayList<>();
        players.add(new HumanPlayer(playerNames.getFirst(), 0));
        players.add(new HumanPlayer(playerNames.getLast(),  1));

        ChessGame game = new ChessGame(players);

        Thread thread = new Thread(game);
        thread.start();

        MainFrame frame = new MainFrame(game);
        game.addObserver(frame);

        SwingUtilities.invokeLater(() -> frame.setVisible(true));

        List<String> moves = tokenizePGN(pgn);

        for (String notation : moves) {
            List<PlayerMove> move = convertPGNToPieceMove(game, notation);
            if (move == null) {
                throw new IllegalArgumentException("Impossible d’interpréter le coup PGN: " + notation);
            }

            game.forceMove(move.getFirst());

            if (move.size() > 1) {
                Piece promotionPiece = game.createPieceFromString(PGNConverter.promotionName, game.nextPlayer());
                game.getBoard().removePieceFromBoard(move.getFirst().destination().getPiece());
                game.getBoard().setPieceToCell(promotionPiece, move.getFirst().destination());
            }
        }

        game.updateAll();
    }

    public static List<String> getPlayersFromPGN(String pgn) {
        ArrayList<String> names = new ArrayList<>();
        names.add(null);
        names.add(null);

        Pattern tagPattern = Pattern.compile("\\[(White|Black)\\s+\"([^\"]*)\"\\]");
        Matcher matcher = tagPattern.matcher(pgn);

        while (matcher.find()) {
            String tag   = matcher.group(1);
            String value = matcher.group(2);
            if ("White".equals(tag)) {
                names.set(0, value);
            } else {
                names.set(1, value);
            }
        }

        return names;
    }
}
