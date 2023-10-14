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
public class EvaluatorStatistics implements GameEvaluator, SearchLifeCycle {
    private final GameEvaluator imp;
    private final GameEvaluatorCache cache;
    private long evaluationsCounter;
    private Set<EvaluationEntry> evaluations;

    @Setter
    @Accessors(chain = true)
    private boolean trackEvaluations;
    private Game game;


    public EvaluatorStatistics(GameEvaluator gameEvaluator) {
        this.imp = gameEvaluator;
        this.cache = gameEvaluator instanceof GameEvaluatorCache gameEvaluatorCache ? gameEvaluatorCache : null;
    }

    @Override
    public int evaluate() {
        evaluationsCounter++;
        int evaluation = imp.evaluate();
        if (trackEvaluations) {
            long hash = game.getChessPosition().getZobristHash();
            evaluations.add(new EvaluationEntry(hash, evaluation));
        }
        return evaluation;
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
        imp.setGame(game);
    }

    @Override
    public void beforeSearch(Game game) {
        evaluationsCounter = 0;
        if (trackEvaluations) {
            evaluations = new LinkedHashSet<>();
        }
        if (cache != null) {
            cache.resetCacheHitsCounter();
        }
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        long cacheHitsCounter = cache != null ? cache.getCacheHitsCounter() : 0;
        result.setEvaluationStatistics(new EvaluationStatistics(evaluationsCounter, cacheHitsCounter, evaluations));
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
