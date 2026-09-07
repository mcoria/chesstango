package net.chesstango.search.builders;

import lombok.Getter;
import net.chesstango.search.ListenerMediator;
import net.chesstango.search.smart.evaluator.EvaluatorCache;
import net.chesstango.search.smart.evaluator.EvaluatorCacheArray;
import net.chesstango.search.smart.evaluator.EvaluatorCacheDebug;
import net.chesstango.search.smart.evaluator.listeners.EvaluatorCacheListener;
import net.chesstango.search.smart.evaluator.visitors.LinkEvaluatorCacheVisitor;

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

    private ListenerMediator listenerMediator;

    private boolean withDebugSearchTree;

    /**
     * Front-end evaluators
     */
    private EvaluatorCache evaluatorCache;

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

    @Override
    public void build() {
        buildObjects();

        setupListenerMediator();

        evaluatorCache = createChains();
    }

    private void buildObjects() {
        if (withDebugSearchTree) {
            evaluatorCacheDebug = new EvaluatorCacheDebug();
        }
    }

    private void setupListenerMediator() {
        listenerMediator.add(evaluatorCacheListener);

        if (evaluatorCacheDebug != null) {
            listenerMediator.add(evaluatorCacheDebug);
        }
    }

    private EvaluatorCache createChains() {
        List<EvaluatorCache> chain = new LinkedList<>();

        if (evaluatorCacheDebug != null) {
            chain.add(evaluatorCacheDebug);
        }

        chain.add(evaluatorCacheArray);

        return linkEvaluatorCacheChain(chain);
    }

    @Override
    public void link() {
        evaluatorCacheListener.setGameEvaluatorCacheArray(evaluatorCacheArray);

        listenerMediator.accept(new LinkEvaluatorCacheVisitor(evaluatorCache));
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
