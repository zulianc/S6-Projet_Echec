package models;

public class Player {
    private String name;
    private Game game;

    public Move getMove() {
        try {
            synchronized (game) {
                wait();
            }
            return game.move;
        } catch (Exception ignored) {
            return null;
        }
    }
}
