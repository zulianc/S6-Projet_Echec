package views;

import models.pieces.Piece;
import models.pieces.PieceName;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class VPiece {

    private static final String BASE = "/assets/ChessPieces/";
    private static final String EXTENSION = ".png";

    private static final Map<String, Image> temp = new HashMap<>();

    public static Image getImage(Piece piece) {
        int color = piece.getColor();
        String pieceName = piece.getPieceName();

        String key = color + "-" + pieceName;

        if (temp.containsKey(key)) {
            return temp.get(key);
        }

        Image img = null;
        try {
            String relativeLink = "../../" + BASE + key + EXTENSION;
            img = ImageIO.read(new File(relativeLink));
            temp.put(key, img);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return img;
    }
}
