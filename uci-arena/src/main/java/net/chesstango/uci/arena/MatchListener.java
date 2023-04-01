package net.chesstango.uci.arena;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.uci.gui.EngineController;

public interface MatchListener {
    void notifyNewGame(Game game, EngineController white, EngineController black);

    void notifyExecutedMove(Game game, Move move);

    void notifyFinishedGame(Game game);
}
