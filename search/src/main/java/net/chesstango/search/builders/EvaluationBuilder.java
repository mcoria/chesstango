package net.chesstango.search.builders;

import lombok.Getter;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.smart.evaluator.EvaluatorCacheArray;
import net.chesstango.search.smart.evaluator.EvaluatorCache;
import net.chesstango.search.ListenerMediator;
import net.chesstango.search.smart.evaluator.EvaluatorCacheDebug;
import net.chesstango.search.smart.evaluator.EvaluatorDebug;
import net.chesstango.search.smart.evaluator.listeners.SetGameToEvaluator;
import net.chesstango.search.smart.evaluator.visitors.LinkEvaluatorCacheVisitor;
import net.chesstango.search.smart.evaluator.visitors.LinkEvaluatorVisitor;
import net.chesstango.search.smart.statistics.evaluation.EvaluationCounters;
import net.chesstango.search.smart.statistics.evaluation.EvaluatorStatisticsCollector;
import net.chesstango.search.smart.statistics.evaluation.listeners.EvaluatorCacheListener;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Corias
 */
public class EvaluationBuilder implements SearchObjectBuilder<EvaluationBuilder> {

    private Evaluator evaluatorImp;
    private EvaluatorDebug evaluatorDebug;
    private SetGameToEvaluator setGameToEvaluator;

    @Getter
    private EvaluatorCacheArray evaluatorCacheArray;
    private EvaluatorCacheDebug evaluatorCacheDebug;
    private EvaluatorCacheListener evaluatorCacheListener;

    private EvaluationCounters evaluationCounters;
    private EvaluatorStatisticsCollector evaluatorStatisticsCollector;

    private ListenerMediator listenerMediator;

    private boolean withDebugSearchTree;
    private boolean withTrackEvaluations;
    private boolean withGameEvaluatorCache;
    private boolean withStatistics;


    /**
     * Front-end evaluators
     */
    private Evaluator evaluator;
    private EvaluatorCache evaluatorCache;

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
    public EvaluationBuilder withSmartListenerMediator(ListenerMediator listenerMediator) {
        this.listenerMediator = listenerMediator;
        return this;
    }

    @Override
    public void build() {
        if(evaluatorImp == null) {
            throw new RuntimeException("You must set a game evaluator");
        }

        buildObjects();

        setupListenerMediator();

        createChains();
    }

    @Override
    public void link() {
        listenerMediator.accept(new LinkEvaluatorVisitor(evaluator));

        if (evaluatorCache != null) {
            listenerMediator.accept(new LinkEvaluatorCacheVisitor(evaluatorCache));
        }
    }

    private void buildObjects() {
        setGameToEvaluator = new SetGameToEvaluator();

        if (withGameEvaluatorCache) {
            evaluatorCacheArray = new EvaluatorCacheArray();

            evaluatorCacheListener = new EvaluatorCacheListener();
            evaluatorCacheListener.setGameEvaluatorCacheArray(evaluatorCacheArray);
        }

        if (withDebugSearchTree) {
            evaluatorDebug = new EvaluatorDebug();
            evaluatorCacheDebug = new EvaluatorCacheDebug();
        }

        if (withStatistics) {
            evaluationCounters = new EvaluationCounters()
                    .setEvaluatorCacheArray(evaluatorCacheArray);  // No importa que sea NULL

            evaluatorStatisticsCollector = new EvaluatorStatisticsCollector()
                    .setEvaluationsCounters(evaluationCounters);
        }
    }

    private void setupListenerMediator() {
        if (setGameToEvaluator != null) {
            listenerMediator.add(setGameToEvaluator);
        }
        if (evaluationCounters != null) {
            listenerMediator.add(evaluationCounters);
        }
        if (evaluatorStatisticsCollector != null) {
            listenerMediator.add(evaluatorStatisticsCollector);
        }
        if (evaluatorCacheListener != null) {
            listenerMediator.add(evaluatorCacheListener);
        }
        if (evaluatorCacheDebug != null) {
            listenerMediator.add(evaluatorCacheDebug);
        }
        if (evaluatorDebug != null) {
            listenerMediator.add(evaluatorDebug);
        }
    }

    private void createChains() {
        evaluator = createEvaluatorChain();

        setGameToEvaluator.setEvaluator(evaluator);

        if (withGameEvaluatorCache) {
            evaluatorCache = createEvaluatorCacheChain();
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

        if (evaluatorCacheArray != null) {
            chain.add(evaluatorCacheArray);
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

                case EvaluatorCacheArray evaluatorCacheArray ->
                        evaluatorCacheArray.setImp(next);

                case EvaluatorDebug evaluatorDebug ->
                        evaluatorDebug.setEvaluator(next);

                case null -> throw new RuntimeException(String.format("evaluator %d is null", i));

                default ->
                        throw new RuntimeException("evaluator not found: " + currentFilter.getClass().getSimpleName());
            }
        }
        return chain.getFirst();
    }


    private EvaluatorCache createEvaluatorCacheChain() {
        List<EvaluatorCache> chain = new LinkedList<>();

        if (evaluatorCacheDebug != null) {
            chain.add(evaluatorCacheDebug);
        }

        chain.add(evaluatorCacheArray);

        return linkEvaluatorCacheChain(chain);
    }

    private EvaluatorCache linkEvaluatorCacheChain(List<EvaluatorCache> chain) {
        for (int i = 0; i < chain.size() - 1; i++) {
            EvaluatorCache currentFilter = chain.get(i);
            EvaluatorCache next = chain.get(i + 1);

            switch (currentFilter) {
                case EvaluatorCacheDebug evaluatorCacheDebug -> evaluatorCacheDebug.setEvaluatorCache(next);

                default ->
                        throw new RuntimeException("evaluator not found: " + currentFilter.getClass().getSimpleName());
            }
        }
        return chain.getFirst();
    }

}
