package views;

import models.pieces.Piece;
import models.pieces.PieceName;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class VPiece {

    private static final String BASE = "assets/ChessPieces/";
    private static final String EXTENSION = ".png";

    private static final Map<String, Image> temp = new HashMap<>();

    public static Image getImage(Piece piece) {
        int colorInt = piece.getColor();
        String color = (colorInt == 0) ? "white" : "black";
        String pieceName = piece.getPieceName();

        String key = color + "-" + pieceName;

        if (temp.containsKey(key)) {
            return temp.get(key);
        }

        Image img = null;
        try {
            String relativeLink = BASE + key + EXTENSION;
            File file = Paths.get(relativeLink).toFile();
            System.out.println(file.getAbsolutePath());
            img = ImageIO.read(file);
            temp.put(key, img);
        } catch (Exception e) {
            System.err.println("Error VPiece : "+e.getMessage());
        }
        return img;
    }
}
