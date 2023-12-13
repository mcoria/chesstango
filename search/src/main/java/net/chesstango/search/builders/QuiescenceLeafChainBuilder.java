package net.chesstango.search.builders;


import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.TranspositionMoveSorterQ;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class QuiescenceLeafChainBuilder {
    private final AlphaBetaTerminal leaf;
    private GameEvaluator gameEvaluator;
    private TranspositionTableQ transpositionTableQ;
    private ZobristTracker zobristQTracker;
    private SmartListenerMediator smartListenerMediator;
    private boolean withZobristTracker;
    private boolean withTranspositionTable;


    public QuiescenceLeafChainBuilder() {
        leaf = new AlphaBetaTerminal();
    }

    public QuiescenceLeafChainBuilder withGameEvaluator(GameEvaluator gameEvaluator) {
        this.gameEvaluator = gameEvaluator;
        return this;
    }


    public QuiescenceLeafChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

    public QuiescenceLeafChainBuilder withTranspositionTable() {
        this.withTranspositionTable = true;
        return this;
    }

    public QuiescenceLeafChainBuilder withSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        this.smartListenerMediator = smartListenerMediator;
        return this;
    }


    /**
     * <p>
     * <p>
     * *  QuiescenceStatics -> ZobristTracker -> TranspositionTableQ -> QuiescenceFlowControl -> Quiescence
     * *            ^                                                                              |
     * *            |                                                                              |
     * *            -------------------------------------------------------------------------------
     *
     * @return
     */
    public AlphaBetaFilter build() {
        buildObjects();

        setupListenerMediator();

        return createChain();
    }

    private void buildObjects() {
        leaf.setGameEvaluator(gameEvaluator);

        if (withZobristTracker) {
            zobristQTracker = new ZobristTracker();
        }

        if(withTranspositionTable){
            transpositionTableQ = new TranspositionTableQ();
        }
    }

    private void setupListenerMediator() {
        if (zobristQTracker != null) {
            smartListenerMediator.add(zobristQTracker);
        }
        if (transpositionTableQ != null) {
            smartListenerMediator.add(transpositionTableQ);
        }
    }

    private AlphaBetaFilter createChain() {


        List<AlphaBetaFilter> chain = new LinkedList<>();

        if (zobristQTracker != null) {
            chain.add(zobristQTracker);
        }

        if (transpositionTableQ != null) {
            chain.add(transpositionTableQ);
        }

        chain.add(leaf);

        for (int i = 0; i < chain.size() - 1; i++) {
            AlphaBetaFilter currentFilter = chain.get(i);
            AlphaBetaFilter next = chain.get(i + 1);

            if (currentFilter instanceof ZobristTracker) {
                zobristQTracker.setNext(next);
            } else if (currentFilter instanceof TranspositionTableQ) {
                transpositionTableQ.setNext(next);
            } else if (currentFilter instanceof AlphaBetaTerminal) {
                //leaf
            } else {
                throw new RuntimeException("filter not found");
            }
        }


        return chain.get(0);
    }

}
