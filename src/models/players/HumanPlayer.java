package models.players;

import models.boards.PlayerMove;

public class HumanPlayer extends Player {

    public HumanPlayer(String name, int team) {
        super(name, team);
    }

    @Override
    public PlayerMove getMove() {
        if (!this.alive) {
            throw new RuntimeException("Human player is dead");
        }

        try {
            synchronized (game) {
                game.wait();
            }
            return game.playerMove;
        } catch (Exception ignored) {
            return null;
        }
    }
}
