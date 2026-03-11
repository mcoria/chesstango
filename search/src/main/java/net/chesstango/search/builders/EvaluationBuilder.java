package net.chesstango.search.builders;

import lombok.Getter;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.EvaluatorCache;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.statistics.evaluation.EvaluatorStatisticsCollector;
import net.chesstango.search.smart.alphabeta.statistics.evaluation.listeners.EvaluatorCacheListener;

/**
 * @author Mauricio Corias
 */
public class EvaluationBuilder {

    private Evaluator evaluatorImp;

    @Getter
    private EvaluatorCache gameEvaluatorCache;

    private EvaluatorStatisticsCollector gameEvaluatorStatisticsCollector;

    private EvaluatorCacheListener evaluatorCacheListener;

    private SearchListenerMediator searchListenerMediator;

    private boolean withTrackEvaluations;
    private boolean withGameEvaluatorCache;
    private boolean withStatistics;


    public EvaluationBuilder withGameEvaluator(Evaluator evaluator) {
        this.evaluatorImp = evaluator;
        return this;
    }

    public EvaluationBuilder withGameEvaluatorCache() {
        withGameEvaluatorCache = true;
        return this;
    }

    public EvaluationBuilder withTrackEvaluations() {
        if (!withStatistics) {
            throw new RuntimeException("You must enable Statistics first");
        }
        withTrackEvaluations = true;
        return this;
    }

    public EvaluationBuilder withStatistics() {
        withStatistics = true;
        return this;
    }

    public EvaluationBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
        return this;
    }


    public Evaluator build() {
        buildObjects();
        setupListenerMediator();
        return createChain();
    }


    private void buildObjects() {
        if (withGameEvaluatorCache) {
            gameEvaluatorCache = new EvaluatorCache(evaluatorImp);
            evaluatorCacheListener = new EvaluatorCacheListener();
            evaluatorCacheListener.setGameEvaluatorCache(gameEvaluatorCache);
        }

        if (withStatistics) {
            gameEvaluatorStatisticsCollector = new EvaluatorStatisticsCollector()
                    .setGameEvaluatorCache(gameEvaluatorCache)
                    .setTrackEvaluations(withTrackEvaluations);
        }
    }

    private void setupListenerMediator() {
        if (gameEvaluatorStatisticsCollector != null) {
            searchListenerMediator.add(gameEvaluatorStatisticsCollector);
        }
        if (evaluatorCacheListener != null) {
            searchListenerMediator.add(evaluatorCacheListener);
        }
    }

    private Evaluator createChain() {
        Evaluator chain = evaluatorImp;
        if (gameEvaluatorCache != null) {
            chain = gameEvaluatorCache;
        }

        if (gameEvaluatorStatisticsCollector != null) {
            gameEvaluatorStatisticsCollector.setImp(chain);
            chain = gameEvaluatorStatisticsCollector;
        }

        return chain;
    }

}
