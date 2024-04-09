package net.chesstango.search.smart.features.statistics.evaluation;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.GameEvaluatorCache;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class GameEvaluatorStatisticsWrapper implements GameEvaluator, SearchByCycleListener {

    @Setter
    @Getter
    @Accessors(chain = true)
    private GameEvaluator imp;

    @Setter
    @Getter
    @Accessors(chain = true)
    private GameEvaluatorCache gameEvaluatorCache;

    @Setter
    @Accessors(chain = true)
    private boolean trackEvaluations;
    private long evaluationsCounter;
    private Set<EvaluationEntry> evaluations;
    private Game game;


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
    public void beforeSearch(SearchByCycleContext context) {
        evaluationsCounter = 0;
        if (trackEvaluations) {
            evaluations = new LinkedHashSet<>();
        }
        if (gameEvaluatorCache != null) {
            gameEvaluatorCache.resetCacheHitsCounter();
        }
    }

    @Override
    public void afterSearch(SearchMoveResult searchMoveResult) {
        long cacheHitsCounter = gameEvaluatorCache != null ? gameEvaluatorCache.getCacheHitsCounter() : 0;
        searchMoveResult.setEvaluationStatistics(new EvaluationStatistics(evaluationsCounter, cacheHitsCounter, evaluations));
    }

}
