package net.chesstango.uci.arena.listeners;

import lombok.Getter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.uci.arena.MatchResult;
import net.chesstango.uci.arena.gui.EngineController;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class CaptureMatchResult implements MatchListener {

    @Getter
    private final List<MatchResult> matchResults;

    public CaptureMatchResult() {
        this.matchResults = Collections.synchronizedList(new LinkedList<>());
    }

    @Override
    public void notifyNewGame(Game game, EngineController white, EngineController black) {
    }

    @Override
    public void notifyMove(Game game, Move move) {
    }

    @Override
    public void notifyEndGame(Game game, MatchResult matchResult) {
        this.matchResults.add(matchResult);
    }

}
