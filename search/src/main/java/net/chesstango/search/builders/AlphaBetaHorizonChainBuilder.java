package net.chesstango.search.builders;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaHorizon;
import net.chesstango.search.smart.alphabeta.filters.TranspositionTable;
import net.chesstango.search.smart.alphabeta.filters.ZobristTracker;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaHorizonChainBuilder {
    private final AlphaBetaHorizon alphaBetaHorizon;
    private SmartListenerMediator smartListenerMediator;
    private GameEvaluator gameEvaluator;
    private AlphaBetaFilter quiescence;
    private TranspositionTable transpositionTable;

    private ZobristTracker zobristTracker;
    private boolean withZobristTracker;
    private boolean withTranspositionTable;

    public AlphaBetaHorizonChainBuilder() {
        alphaBetaHorizon = new AlphaBetaHorizon();
    }

    public AlphaBetaHorizonChainBuilder withTranspositionTable() {
        this.withTranspositionTable = true;
        return this;
    }


    public AlphaBetaHorizonChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

    public AlphaBetaHorizonChainBuilder withSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        this.smartListenerMediator = smartListenerMediator;
        return this;
    }

    public AlphaBetaHorizonChainBuilder withGameEvaluator(GameEvaluator gameEvaluator) {
        this.gameEvaluator = gameEvaluator;
        return this;
    }

    public AlphaBetaHorizonChainBuilder withQuiescence(AlphaBetaFilter quiescenceChain) {
        this.quiescence = quiescenceChain;
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
        alphaBetaHorizon.setGameEvaluator(gameEvaluator);

        if (withZobristTracker) {
            zobristTracker = new ZobristTracker();
        }

        if (withTranspositionTable) {
            transpositionTable = new TranspositionTable();
        }
    }

    private void setupListenerMediator() {
        smartListenerMediator.add(alphaBetaHorizon);

        if (zobristTracker != null) {
            smartListenerMediator.add(zobristTracker);
        }
        if (transpositionTable != null) {
            smartListenerMediator.add(transpositionTable);
        }
    }

    private AlphaBetaFilter createChain() {
        List<AlphaBetaFilter> chain = new LinkedList<>();

        if (zobristTracker != null) {
            chain.add(zobristTracker);
        }
        if (transpositionTable != null) {
            chain.add(transpositionTable);
        }

        chain.add(alphaBetaHorizon);

        chain.add(quiescence);

        for (int i = 0; i < chain.size() - 1; i++) {
            AlphaBetaFilter currentFilter = chain.get(i);
            AlphaBetaFilter next = chain.get(i + 1);

            if (currentFilter instanceof ZobristTracker) {
                zobristTracker.setNext(next);
            } else if (currentFilter instanceof TranspositionTable) {
                transpositionTable.setNext(next);
            } else if (currentFilter instanceof AlphaBetaHorizon) {
                alphaBetaHorizon.setQuiescence(next);
            } else {
                throw new RuntimeException("filter not found");
            }
        }

        return chain.get(0);
    }
}