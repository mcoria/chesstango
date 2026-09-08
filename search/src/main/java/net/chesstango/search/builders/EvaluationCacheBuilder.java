package net.chesstango.search.builders;

import lombok.Getter;
import net.chesstango.search.ListenerMediator;
import net.chesstango.search.smart.evaluator.EvaluatorCache;
import net.chesstango.search.smart.evaluator.EvaluatorCacheArray;
import net.chesstango.search.smart.evaluator.EvaluatorCacheDebug;
import net.chesstango.search.smart.evaluator.listeners.EvaluatorCacheListener;
import net.chesstango.search.smart.evaluator.visitors.LinkEvaluatorCacheVisitor;
import net.chesstango.search.smart.statistics.evaluation.EvaluatorCacheStatisticsComparatorCollector;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Corias
 */
public class EvaluationCacheBuilder implements SearchObjectBuilder<EvaluationCacheBuilder> {
    @Getter
    private final EvaluatorCacheArray evaluatorCacheArray;
    private final EvaluatorCacheListener evaluatorCacheListener;
    private EvaluatorCacheDebug evaluatorCacheDebug;
    private EvaluatorCacheStatisticsComparatorCollector evaluatorCacheStatisticsComparatorCollector;

    private ListenerMediator listenerMediator;

    private boolean withDebugSearchTree;
    private boolean withStatistics;

    /**
     * Front-end evaluators
     */
    private EvaluatorCache evaluatorCacheNode;
    private EvaluatorCache evaluatorCacheComparator;

    public EvaluationCacheBuilder() {
        evaluatorCacheArray = new EvaluatorCacheArray();
        evaluatorCacheListener = new EvaluatorCacheListener();
    }

    @Override
    public EvaluationCacheBuilder withSmartListenerMediator(ListenerMediator listenerMediator) {
        this.listenerMediator = listenerMediator;
        return this;
    }

    public EvaluationCacheBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    public EvaluationCacheBuilder withStatistics() {
        this.withStatistics = true;
        return this;
    }

    @Override
    public void build() {
        buildObjects();

        setupListenerMediator();

        evaluatorCacheNode = createNodeChain();
        evaluatorCacheComparator = createComparatorChain();
    }


    private void buildObjects() {
        if (withDebugSearchTree) {
            evaluatorCacheDebug = new EvaluatorCacheDebug();
        }

        if (withStatistics) {
            evaluatorCacheStatisticsComparatorCollector = new EvaluatorCacheStatisticsComparatorCollector();
        }
    }

    private void setupListenerMediator() {
        listenerMediator.add(evaluatorCacheListener);

        if (evaluatorCacheDebug != null) {
            listenerMediator.add(evaluatorCacheDebug);
        }
        if (evaluatorCacheStatisticsComparatorCollector != null) {
            listenerMediator.add(evaluatorCacheStatisticsComparatorCollector);
        }
    }

    private EvaluatorCache createNodeChain() {
        List<EvaluatorCache> chain = new LinkedList<>();

        chain.add(evaluatorCacheArray);

        return linkEvaluatorCacheChain(chain);
    }

    private EvaluatorCache createComparatorChain() {
        List<EvaluatorCache> chain = new LinkedList<>();

        if (evaluatorCacheDebug != null) {
            chain.add(evaluatorCacheDebug);
        }

        if (evaluatorCacheStatisticsComparatorCollector != null) {
            chain.add(evaluatorCacheStatisticsComparatorCollector);
        }

        chain.add(evaluatorCacheArray);

        return linkEvaluatorCacheChain(chain);
    }

    @Override
    public void link() {
        evaluatorCacheListener.setGameEvaluatorCacheArray(evaluatorCacheArray);

        listenerMediator.accept(new LinkEvaluatorCacheVisitor(evaluatorCacheNode));
    }


    private EvaluatorCache linkEvaluatorCacheChain(List<EvaluatorCache> chain) {
        for (int i = 0; i < chain.size() - 1; i++) {
            EvaluatorCache currentFilter = chain.get(i);
            EvaluatorCache next = chain.get(i + 1);

            switch (currentFilter) {
                case EvaluatorCacheDebug evaluatorCacheDebug -> evaluatorCacheDebug.setEvaluatorCache(next);

                case EvaluatorCacheStatisticsComparatorCollector evaluatorCacheStatisticsComparatorCollector -> evaluatorCacheStatisticsComparatorCollector.setEvaluatorCache(next);

                default ->
                        throw new RuntimeException("evaluator not found: " + currentFilter.getClass().getSimpleName());
            }
        }
        return chain.getFirst();
    }
}
