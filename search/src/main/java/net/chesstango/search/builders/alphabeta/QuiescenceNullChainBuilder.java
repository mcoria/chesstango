package net.chesstango.search.builders.alphabeta;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaEvaluation;
import net.chesstango.search.smart.features.zobrist.filters.ZobristTracker;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class QuiescenceNullChainBuilder {
    private final AlphaBetaEvaluation alphaBetaEvaluation;
    private Evaluator evaluator;
    private ZobristTracker zobristTracker;
    private SearchListenerMediator searchListenerMediator;

    private boolean withZobristTracker;

    public QuiescenceNullChainBuilder() {
        alphaBetaEvaluation = new AlphaBetaEvaluation();
    }

    public QuiescenceNullChainBuilder withGameEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
        return this;
    }


    public QuiescenceNullChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

    public QuiescenceNullChainBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
        return this;
    }


    /**
     * @return
     */
    public AlphaBetaFilter build() {
        buildObjects();

        setupListenerMediator();

        return createChain();
    }

    private void buildObjects() {
        alphaBetaEvaluation.setEvaluator(evaluator);

        if (withZobristTracker) {
            zobristTracker = new ZobristTracker();
        }
    }

    private void setupListenerMediator() {
        if (zobristTracker != null) {
            searchListenerMediator.add(zobristTracker);
        }
    }


    private AlphaBetaFilter createChain() {

        List<AlphaBetaFilter> chain = new LinkedList<>();

        if (zobristTracker != null) {
            chain.add(zobristTracker);
        }

        chain.add(alphaBetaEvaluation);

        for (int i = 0; i < chain.size() - 1; i++) {
            AlphaBetaFilter currentFilter = chain.get(i);
            AlphaBetaFilter next = chain.get(i + 1);

            if (currentFilter instanceof ZobristTracker) {
                zobristTracker.setNext(next);
            } else if (currentFilter instanceof AlphaBeta) {
                //alphaBetaTerminal.setNext(next);
            } else {
                throw new RuntimeException("filter not found");
            }
        }

        return chain.get(0);
    }
}
