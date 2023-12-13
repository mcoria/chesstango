package net.chesstango.search.builders;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaTerminal;
import net.chesstango.search.smart.alphabeta.filters.ZobristTracker;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class QuiescenceNullChainBuilder {
    private final AlphaBetaTerminal alphaBetaTerminal;
    private GameEvaluator gameEvaluator;
    private ZobristTracker zobristTracker;
    private SmartListenerMediator smartListenerMediator;

    private boolean withZobristTracker;

    public QuiescenceNullChainBuilder() {
        alphaBetaTerminal = new AlphaBetaTerminal();
    }

    public QuiescenceNullChainBuilder withGameEvaluator(GameEvaluator gameEvaluator) {
        this.gameEvaluator = gameEvaluator;
        return this;
    }


    public QuiescenceNullChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

    public QuiescenceNullChainBuilder withSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        this.smartListenerMediator = smartListenerMediator;
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
        alphaBetaTerminal.setGameEvaluator(gameEvaluator);

        if (withZobristTracker) {
            zobristTracker = new ZobristTracker();
        }
    }

    private void setupListenerMediator() {
        if (zobristTracker != null) {
            smartListenerMediator.add(zobristTracker);
        }
    }


    private AlphaBetaFilter createChain() {

        List<AlphaBetaFilter> chain = new LinkedList<>();

        if (zobristTracker != null) {
            chain.add(zobristTracker);
        }

        chain.add(alphaBetaTerminal);

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
