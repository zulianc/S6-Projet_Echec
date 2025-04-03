package models;

public class Player {
    private String name;
    private Game game;

    public Player(String name, Game game) {
        this.name = name;
        this.game = game;
    }

    public Player(String name) {
        this(name, null);
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

    public void setName(String name) {
        this.name = name;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
