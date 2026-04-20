package net.chesstango.search.builders;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.EvaluatorCache;
import net.chesstango.evaluation.EvaluatorCacheRead;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.evaluator.EvaluatorCacheDebug;
import net.chesstango.search.smart.alphabeta.evaluator.EvaluatorDebug;
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
public class EvaluationBuilder implements SearchObjectBuilder<EvaluationBuilder> {

    private Evaluator evaluatorImp;
    private EvaluatorDebug evaluatorDebug;
    private SetGameToEvaluator setGameToEvaluator;

    private EvaluatorCache evaluatorCache;
    private EvaluatorCacheDebug evaluatorCacheDebug;
    private EvaluatorCacheListener evaluatorCacheListener;

    private EvaluationCounters evaluationCounters;
    private EvaluatorStatisticsCollector evaluatorStatisticsCollector;

    private SearchListenerMediator searchListenerMediator;

    private boolean withDebugSearchTree;
    private boolean withTrackEvaluations;
    private boolean withGameEvaluatorCache;
    private boolean withStatistics;


    /**
     * Front-end evaluators
     */
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

    @Override
    public EvaluationBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
        return this;
    }

    @Override
    public void build() {
        buildObjects();

        setupListenerMediator();

        createChains();
    }

    @Override
    public void link() {
        searchListenerMediator.accept(new LinkEvaluatorVisitor(evaluator));

        if (evaluatorCacheRead != null) {
            searchListenerMediator.accept(new LinkEvaluatorCacheVisitor(evaluatorCacheRead));
        }
    }

    private void buildObjects() {
        setGameToEvaluator = new SetGameToEvaluator();

        if (withGameEvaluatorCache) {
            evaluatorCache = new EvaluatorCache();

            evaluatorCacheListener = new EvaluatorCacheListener();
            evaluatorCacheListener.setGameEvaluatorCache(evaluatorCache);
        }

        if (withDebugSearchTree) {
            evaluatorDebug = new EvaluatorDebug();
            evaluatorCacheDebug = new EvaluatorCacheDebug();
        }

        if (withStatistics) {
            evaluationCounters = new EvaluationCounters()
                    .setEvaluatorCache(evaluatorCache);  // No importa que sea NULL

            evaluatorStatisticsCollector = new EvaluatorStatisticsCollector()
                    .setEvaluationsCounters(evaluationCounters);
        }
    }

    private void setupListenerMediator() {
        if (setGameToEvaluator != null) {
            searchListenerMediator.add(setGameToEvaluator);
        }
        if (evaluationCounters != null) {
            searchListenerMediator.add(evaluationCounters);
        }
        if (evaluatorStatisticsCollector != null) {
            searchListenerMediator.add(evaluatorStatisticsCollector);
        }
        if (evaluatorCacheListener != null) {
            searchListenerMediator.add(evaluatorCacheListener);
        }
        if (evaluatorCacheDebug != null) {
            searchListenerMediator.add(evaluatorCacheDebug);
        }
        if (evaluatorDebug != null) {
            searchListenerMediator.add(evaluatorDebug);
        }
    }

    private void createChains() {
        evaluator = createEvaluatorChain();

        setGameToEvaluator.setEvaluator(evaluator);

        if (withGameEvaluatorCache) {
            evaluatorCacheRead = createEvaluatorCacheChain();
        }
    }

    private Evaluator createEvaluatorChain() {
        List<Evaluator> chain = new LinkedList<>();

        if (evaluatorStatisticsCollector != null) {
            chain.add(evaluatorStatisticsCollector);
        }

        if (evaluatorDebug != null) {
            chain.add(evaluatorDebug);
        }

        if (evaluatorCache != null) {
            chain.add(evaluatorCache);
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

                case EvaluatorCache evaluatorCache ->
                        evaluatorCache.setImp(next);

                case EvaluatorDebug evaluatorDebug ->
                        evaluatorDebug.setEvaluator(next);

                case null -> throw new RuntimeException(String.format("evaluator %d is null", i));

                default ->
                        throw new RuntimeException("evaluator not found: " + currentFilter.getClass().getSimpleName());
            }
        }
        return chain.getFirst();
    }


    private EvaluatorCacheRead createEvaluatorCacheChain() {
        List<EvaluatorCacheRead> chain = new LinkedList<>();

        if (evaluatorCacheDebug != null) {
            chain.add(evaluatorCacheDebug);
        }

        chain.add(evaluatorCache);

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
