package models.players;

import models.games.Game;
import models.boards.PieceMove;

public abstract class Player {
    protected final String name;
    protected Game game;
    protected final int team;
    protected boolean alive;

    public Player(String name, Game game, int team) {
        this.name = name;
        this.game = game;
        this.team = team;
        this.alive = false;
    }

    public Player(String name, int team) {
        this(name, null, team);
    }

    public Player(int team) {
        this("unnamed player", team);
    }

    public abstract PieceMove getMove();

    public void startGame(Game game) {
        this.game = game;
        this.alive = true;
    }

    public void playerLostGame() {
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    public String getName() {
        return name;
    }

    public int getTeam() {
        return team;
    }
}
