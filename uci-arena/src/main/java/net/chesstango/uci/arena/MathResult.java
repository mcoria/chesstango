package net.chesstango.uci.arena;

import net.chesstango.board.Game;
import net.chesstango.uci.gui.EngineController;

/**
 * @author Mauricio Coria
 *
 */
public class MathResult {
    private final Game game;

    private final EngineController engineWhite;

    private final EngineController engineBlack;

    private final int points;


    public MathResult(Game game, EngineController engineWhite, EngineController engineBlack, int points) {
        this.game = game;
        this.engineWhite = engineWhite;
        this.engineBlack = engineBlack;
        this.points = points;
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


}
