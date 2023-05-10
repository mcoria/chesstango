package net.chesstango.search.smart.alphabeta.actions;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchActions;
import net.chesstango.search.smart.SearchContext;

import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class MoveEvaluations implements SearchActions {
    private Map<Long, SearchContext.TableEntry> maxMap;
    private Map<Long, SearchContext.TableEntry> minMap;
    private Game game;
    private int maxPly;

    @Override
    public void init(Game game, SearchContext context) {
        this.game = game;
        this.maxPly = context.getMaxPly();
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();
    }

    @Override
    public void close(SearchMoveResult result) {
        result.storeMoveEvaluations(game, maxPly, maxMap, minMap);
    }
}
