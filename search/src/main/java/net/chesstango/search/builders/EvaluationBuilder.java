package net.chesstango.search.builders;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.EvaluatorCache;
import net.chesstango.evaluation.EvaluatorCacheRead;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.evaluator.EvaluatorCacheDebug;
import net.chesstango.search.smart.alphabeta.evaluator.listeners.SetGameToEvaluator;
import net.chesstango.search.smart.alphabeta.evaluator.visitors.LinkEvaluatorCacheVisitor;
import net.chesstango.search.smart.alphabeta.evaluator.visitors.LinkEvaluatorVisitor;
import net.chesstango.search.smart.alphabeta.statistics.evaluation.EvaluationCounters;
import net.chesstango.search.smart.alphabeta.statistics.evaluation.EvaluatorStatisticsCollector;
import net.chesstango.search.smart.alphabeta.statistics.evaluation.listeners.EvaluatorCacheListener;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Corias
 */
public class EvaluationBuilder {

    private Evaluator evaluatorImp;
    private SetGameToEvaluator setGameToEvaluator;

    private EvaluatorCache gameEvaluatorCache;
    private EvaluatorCacheDebug gameEvaluatorCacheDebug;
    private EvaluatorCacheListener evaluatorCacheListener;

    private EvaluationCounters evaluationCounters;
    private EvaluatorStatisticsCollector gameEvaluatorStatisticsCollector;

    private SearchListenerMediator searchListenerMediator;

    private boolean withDebugSearchTree;
    private boolean withTrackEvaluations;
    private boolean withGameEvaluatorCache;
    private boolean withStatistics;

    private Evaluator evaluator;
    private EvaluatorCacheRead evaluatorCacheRead;

    public EvaluationBuilder withGameEvaluator(Evaluator evaluator) {
        this.evaluatorImp = evaluator;
        return this;
    }

    public EvaluationBuilder withGameEvaluatorCache() {
        this.withGameEvaluatorCache = true;
        return this;
    }

    public EvaluationBuilder withTrackEvaluations() {
        if (!withStatistics) {
            throw new RuntimeException("You must enable Statistics first");
        }
        this.withTrackEvaluations = true;
        return this;
    }

    public EvaluationBuilder withStatistics() {
        this.withStatistics = true;
        return this;
    }

    public EvaluationBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    public EvaluationBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
        return this;
    }

    public void build() {
        buildObjects();

        setupListenerMediator();

        evaluator = createEvaluatorChain();

        setGameToEvaluator.setEvaluator(evaluator);

        if (withGameEvaluatorCache) {
            evaluatorCacheRead = createEvaluatorCacheChain();
        }
    }

    public void link() {
        searchListenerMediator.accept(new LinkEvaluatorVisitor(evaluator));

        if (withGameEvaluatorCache) {
            searchListenerMediator.accept(new LinkEvaluatorCacheVisitor(evaluatorCacheRead));
        }
    }

    private void buildObjects() {
        setGameToEvaluator = new SetGameToEvaluator();

        if (withDebugSearchTree) {
            gameEvaluatorCacheDebug = new EvaluatorCacheDebug();
            gameEvaluatorCacheDebug.setEvaluatorCacheRead(gameEvaluatorCache);
        }

        if (withGameEvaluatorCache) {
            gameEvaluatorCache = new EvaluatorCache();

            evaluatorCacheListener = new EvaluatorCacheListener();
            evaluatorCacheListener.setGameEvaluatorCache(gameEvaluatorCache);
        }

        if (withStatistics) {
            evaluationCounters = new EvaluationCounters();

            gameEvaluatorStatisticsCollector = new EvaluatorStatisticsCollector()
                    .setEvaluationsCounters(evaluationCounters)
                    .setTrackEvaluations(withTrackEvaluations);
        }
    }

    private void setupListenerMediator() {
        if (setGameToEvaluator != null) {
            searchListenerMediator.add(setGameToEvaluator);
        }
        if (evaluationCounters != null) {
            searchListenerMediator.add(evaluationCounters);
        }
        if (gameEvaluatorStatisticsCollector != null) {
            searchListenerMediator.add(gameEvaluatorStatisticsCollector);
        }
        if (evaluatorCacheListener != null) {
            searchListenerMediator.add(evaluatorCacheListener);
        }
        if (gameEvaluatorCacheDebug != null) {
            searchListenerMediator.add(gameEvaluatorCacheDebug);
        }
    }

    private Evaluator createEvaluatorChain() {
        List<Evaluator> chain = new LinkedList<>();

        if (gameEvaluatorStatisticsCollector != null) {
            chain.add(gameEvaluatorStatisticsCollector);
        }

        if (gameEvaluatorCache != null) {
            chain.add(gameEvaluatorCache);
        }

        chain.add(evaluatorImp);

        return linkEvaluatorChain(chain);
    }

    private Evaluator linkEvaluatorChain(List<Evaluator> chain) {
        for (int i = 0; i < chain.size() - 1; i++) {
            Evaluator currentFilter = chain.get(i);
            Evaluator next = chain.get(i + 1);

            switch (currentFilter) {
                case EvaluatorStatisticsCollector evaluatorStatisticsCollector ->
                        evaluatorStatisticsCollector.setImp(next);

                case EvaluatorCache evaluatorCache -> evaluatorCache.setImp(next);

                case null -> throw new RuntimeException(String.format("evaluator %d is null", i));

                default ->
                        throw new RuntimeException("evaluator not found: " + currentFilter.getClass().getSimpleName());
            }
        }
        return chain.getFirst();
    }


    private EvaluatorCacheRead createEvaluatorCacheChain() {
        List<EvaluatorCacheRead> chain = new LinkedList<>();

        if (gameEvaluatorCacheDebug != null) {
            chain.add(gameEvaluatorCacheDebug);
        }

        chain.add(gameEvaluatorCache);

        return linkEvaluatorCacheChain(chain);
    }

    private EvaluatorCacheRead linkEvaluatorCacheChain(List<EvaluatorCacheRead> chain) {
        for (int i = 0; i < chain.size() - 1; i++) {
            EvaluatorCacheRead currentFilter = chain.get(i);
            EvaluatorCacheRead next = chain.get(i + 1);

            switch (currentFilter) {
                case EvaluatorCacheDebug evaluatorCacheDebug -> evaluatorCacheDebug.setEvaluatorCacheRead(next);

                default ->
                        throw new RuntimeException("evaluator not found: " + currentFilter.getClass().getSimpleName());
            }
        }
        return chain.getFirst();
    }

}
