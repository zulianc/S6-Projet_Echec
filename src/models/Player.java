package models;

public class Player {
    private String name;
    private final Game game;

    public Player(String name, Game game) {
        this.name = name;
        this.game = game;
    }

    public Move getMove() {
        try {
            synchronized (game) {
                game.wait();
            }
            return game.move;
        } catch (Exception ignored) {
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public Game getGame() {
        return game;
    }
}
