package net.chesstango.uci.arena.listeners;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.uci.arena.GameResult;
import net.chesstango.uci.arena.MatchListener;
import net.chesstango.uci.gui.EngineController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MatchBroadcaster implements MatchListener {

    private List<MatchListener> matchListenerList = new ArrayList<>();

    @Override
    public void notifyNewGame(Game game, EngineController white, EngineController black) {
        matchListenerList.forEach(listener -> listener.notifyNewGame(game, white, black));
    }

    @Override
    public void notifyMove(Game game, Move move) {
        matchListenerList.forEach(listener -> listener.notifyMove(game, move));
    }

    @Override
    public void notifyEndGame(GameResult gameResult) {
        matchListenerList.forEach(listener -> listener.notifyEndGame(gameResult));
    }

    public MatchBroadcaster addListener(MatchListener listener){
        matchListenerList.add(listener);
        return this;
    }
}
