package models;

public class Player {
    private String name;
    private Game game;
    private final int team;

    public Player(String name, Game game, int team) {
        this.name = name;
        this.game = game;
        this.team = team;
    }

    public Player(String name, int team) {
        this(name, null, team);
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

    public int getTeam() {
        return team;
    }
}
