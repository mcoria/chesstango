package net.chesstango.search.builders;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.ListenerMediator;
import net.chesstango.search.smart.evaluator.EvaluatorCacheAdapter;
import net.chesstango.search.smart.evaluator.EvaluatorDebug;
import net.chesstango.search.smart.evaluator.listeners.SetGameToEvaluator;
import net.chesstango.search.smart.evaluator.visitors.LinkEvaluatorVisitor;
import net.chesstango.search.smart.statistics.evaluator.EvaluatorCounters;
import net.chesstango.search.smart.statistics.evaluator.EvaluatorStatisticsCollector;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Corias
 */
public class EvaluationBuilder implements SearchObjectBuilder<EvaluationBuilder> {

    private Evaluator evaluatorImp;
    private EvaluatorDebug evaluatorDebug;
    private EvaluatorCacheAdapter evaluatorCacheAdapter;

    // Statistics
    private EvaluatorCounters evaluatorCounters;
    private EvaluatorStatisticsCollector evaluatorStatisticsCollector;

    private SetGameToEvaluator setGameToEvaluator;

    private ListenerMediator listenerMediator;

    private boolean withDebugSearchTree;
    private boolean withTrackEvaluations;
    private boolean withStatistics;
    private boolean withGameEvaluatorCache;


    /**
     * Front-end evaluators
     */
    private Evaluator evaluator;

    public EvaluationBuilder withGameEvaluator(Evaluator evaluator) {
        this.evaluatorImp = evaluator;
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

    public EvaluationBuilder withGameEvaluatorCache() {
        this.withGameEvaluatorCache = true;
        return this;
    }

    @Override
    public EvaluationBuilder withSmartListenerMediator(ListenerMediator listenerMediator) {
        this.listenerMediator = listenerMediator;
        return this;
    }

    @Override
    public void build() {
        if (evaluatorImp == null) {
            throw new RuntimeException("You must set a game evaluator");
        }

        buildObjects();

        setupListenerMediator();

        createChains();
    }

    private void buildObjects() {
        setGameToEvaluator = new SetGameToEvaluator();

        if (withDebugSearchTree) {
            evaluatorDebug = new EvaluatorDebug();
        }

        if (withStatistics) {
            evaluatorCounters = new EvaluatorCounters();
            evaluatorStatisticsCollector = new EvaluatorStatisticsCollector();
        }

        if (withGameEvaluatorCache) {
            evaluatorCacheAdapter = new EvaluatorCacheAdapter();
        }
    }

    private void setupListenerMediator() {
        if (setGameToEvaluator != null) {
            listenerMediator.add(setGameToEvaluator);
        }
        if (evaluatorCounters != null) {
            listenerMediator.add(evaluatorCounters);
        }
        if (evaluatorStatisticsCollector != null) {
            listenerMediator.add(evaluatorStatisticsCollector);
        }
        if (evaluatorDebug != null) {
            listenerMediator.add(evaluatorDebug);
        }
        if (evaluatorCacheAdapter != null) {
            listenerMediator.add(evaluatorCacheAdapter);
        }
    }

    private void createChains() {
        evaluator = createEvaluatorChain();

        setGameToEvaluator.setEvaluator(evaluator);
    }

    private Evaluator createEvaluatorChain() {
        List<Evaluator> chain = new LinkedList<>();

        if (evaluatorDebug != null) {
            chain.add(evaluatorDebug);
        }

        if (evaluatorCacheAdapter != null) {
            chain.add(evaluatorCacheAdapter);
        }

        /**
         * En este orden solo captura el numero de veces que realmente se evalua.
         * Llega a esta punto cuando la evaluacion no se encuentra en cache
         */
        if (evaluatorStatisticsCollector != null) {
            chain.add(evaluatorStatisticsCollector);
        }

        chain.add(evaluatorImp);

        return linkEvaluatorChain(chain);
    }

    @Override
    public void link() {
        if (withStatistics) {
            evaluatorStatisticsCollector.setEvaluationsCounters(evaluatorCounters);
        }

        listenerMediator.accept(new LinkEvaluatorVisitor(evaluator));
    }

    private Evaluator linkEvaluatorChain(List<Evaluator> chain) {
        for (int i = 0; i < chain.size() - 1; i++) {
            Evaluator currentFilter = chain.get(i);
            Evaluator next = chain.get(i + 1);

            switch (currentFilter) {
                case EvaluatorStatisticsCollector evaluatorStatisticsCollector ->
                        evaluatorStatisticsCollector.setEvaluator(next);

                case EvaluatorDebug evaluatorDebug -> evaluatorDebug.setEvaluator(next);

                case EvaluatorCacheAdapter evaluatorCacheAdapter -> evaluatorCacheAdapter.setEvaluator(next);

                case null -> throw new RuntimeException(String.format("evaluator %d is null", i));

                default ->
                        throw new RuntimeException("evaluator not found: " + currentFilter.getClass().getSimpleName());
            }
        }
        return chain.getFirst();
    }

}
