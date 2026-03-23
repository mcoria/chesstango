package net.chesstango.search.builders;

import lombok.Getter;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.EvaluatorCache;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.statistics.evaluation.EvaluatorStatisticsCollector;
import net.chesstango.search.smart.alphabeta.statistics.evaluation.listeners.EvaluatorCacheListener;

import java.util.LinkedList;
import java.util.List;

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
            gameEvaluatorCache = new EvaluatorCache();
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
        List<Evaluator> chain = new LinkedList<>();

        if (gameEvaluatorStatisticsCollector != null) {
            chain.add(gameEvaluatorStatisticsCollector);
        }

        if (gameEvaluatorCache != null) {
            chain.add(gameEvaluatorCache);
        }

        chain.add(evaluatorImp);

        return linkChain(chain);
    }

    private Evaluator linkChain(List<Evaluator> chain) {
        for (int i = 0; i < chain.size() - 1; i++) {
            Evaluator currentFilter = chain.get(i);
            Evaluator next = chain.get(i + 1);

            switch (currentFilter) {
                case EvaluatorCache evaluatorCache -> evaluatorCache.setImp(next);

                case EvaluatorStatisticsCollector evaluatorStatisticsCollector ->
                        evaluatorStatisticsCollector.setImp(next);

                case null -> throw new RuntimeException(String.format("evaluator %d is null", i));

                default ->
                        throw new RuntimeException("evaluator not found: " + currentFilter.getClass().getSimpleName());
            }
        }
        return chain.getFirst();
    }

}
