package net.chesstango.uci.arena;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.uci.gui.EngineController;

/**
 * @author Mauricio Coria
 */
public interface MatchListener {
    void notifyNewGame(Game game, EngineController white, EngineController black);

    void notifyExecutedMove(Game game, Move move);

}
