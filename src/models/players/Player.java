package models.players;

import models.Game;
import models.Move;

public abstract class Player {
    protected String name;
    protected Game game;
    protected final int team;
    protected boolean lost = false;

    public Player(String name, Game game, int team) {
        this.name = name;
        this.game = game;
        this.team = team;
    }

    public Player(String name, int team) {
        this(name, null, team);
    }

    public Player(int team) {
        this(null, team);
    }

    public abstract Move getMove();

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
        this.lost = false;
    }

    public int getTeam() {
        return team;
    }

    public void makePlayerLose() {
        lost = true;
    }

    public boolean isAlive() {
        return !lost;
    }
}
