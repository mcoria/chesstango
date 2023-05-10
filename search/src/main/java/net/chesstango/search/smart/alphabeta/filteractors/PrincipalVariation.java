package net.chesstango.search.smart.alphabeta.filteractors;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.FilterActions;
import net.chesstango.search.smart.SearchContext;

import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class PrincipalVariation implements FilterActions {
    private Map<Long, SearchContext.TableEntry> maxMap;
    private Map<Long, SearchContext.TableEntry> minMap;
    private Map<Long, SearchContext.TableEntry> qMaxMap;
    private Map<Long, SearchContext.TableEntry> qMinMap;
    private Game game;
    private int maxPly;

    @Override
    public void init(Game game, SearchContext context) {
        this.game = game;
        this.maxPly = context.getMaxPly();
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();
        this.qMaxMap = context.getQMaxMap();
        this.qMinMap = context.getQMinMap();
    }

    @Override
    public void close(SearchMoveResult result) {
        result.calculatePrincipalVariation(game, maxPly, maxMap, minMap, qMaxMap, qMinMap);
    }
}
