package net.chesstango.uci.arena;

import net.chesstango.board.Game;
import net.chesstango.uci.gui.EngineController;

/**
 * @author Mauricio Coria
 *
 */
public class GameResult {
    private final Game game;
    private final EngineController engineWhite;
    private final EngineController engineBlack;
    private final EngineController winner;

    private final int points;

    public GameResult(Game game, EngineController engineWhite, EngineController engineBlack, EngineController winner, int points) {
        this.game = game;
        this.engineWhite = engineWhite;
        this.engineBlack = engineBlack;
        this.winner = winner;
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

    public EngineController getWinner() {
        return winner;
    }

}
