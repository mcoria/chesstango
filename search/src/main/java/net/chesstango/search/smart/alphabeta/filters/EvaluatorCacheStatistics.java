package net.chesstango.search.smart.alphabeta.filters;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.GameEvaluatorCache;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;
import net.chesstango.search.smart.statistics.EvaluationEntry;
import net.chesstango.search.smart.statistics.EvaluationStatistics;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class EvaluatorCacheStatistics implements GameEvaluator, SearchLifeCycle {
    private final GameEvaluatorCache gameEvaluatorCache;
    private Set<EvaluationEntry> evaluations;

    @Setter
    @Accessors(chain = true)
    private boolean trackEvaluations;


    public EvaluatorCacheStatistics(GameEvaluatorCache instance) {
        this.gameEvaluatorCache = instance;
    }

    @Override
    public int evaluate(Game game) {
        int evaluation = gameEvaluatorCache.evaluate(game);
        if (trackEvaluations) {
            evaluations.add(new EvaluationEntry(game.getChessPosition().getZobristHash(), evaluation));
        }
        return evaluation;
    }

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        gameEvaluatorCache.resetStatisticCounter();
        if (trackEvaluations) {
            evaluations = new LinkedHashSet<>();
        }
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        if (result != null) {
            result.setEvaluationStatistics(new EvaluationStatistics(gameEvaluatorCache.getStatisticCounter(), evaluations));
        }
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }

    @Override
    public void stopSearching() {
    }

    @Override
    public void reset() {
    }

}
