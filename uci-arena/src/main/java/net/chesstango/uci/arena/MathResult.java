package net.chesstango.uci.arena;

import net.chesstango.board.Game;

/**
 * @author Mauricio Coria
 *
 */
public class MathResult {
    private final Game game;

    private final EngineController engineWhite;

    private final EngineController engineBlack;

    private int points;


    public MathResult(Game game, EngineController engineWhite, EngineController engineBlack) {
        this.game = game;
        this.engineWhite = engineWhite;
        this.engineBlack = engineBlack;
    }

    public int getPoints() {
        return points;
    }

    public EngineController getEngineWhite() {
        return engineWhite;
    }

    public EngineController getEngineBlack() {
        return engineBlack;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}
