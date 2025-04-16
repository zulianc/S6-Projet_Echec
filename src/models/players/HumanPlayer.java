package models.players;

import models.Move;

public class HumanPlayer extends Player {

    public HumanPlayer(String name, int team) {
        super(name, team);
    }

    @Override
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
}
