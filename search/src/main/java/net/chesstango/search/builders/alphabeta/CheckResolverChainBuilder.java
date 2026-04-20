package net.chesstango.search.builders.alphabeta;


import net.chesstango.search.builders.sorters.MoveSorterInteriorBuilder;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.pv.filters.UpdatePV;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.AlphaBetaInteriorNodeVisited;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTableQ;
import net.chesstango.search.smart.alphabeta.zobrist.filters.ZobristTracker;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class CheckResolverChainBuilder extends AbstractChainBuilder {
    private final AlphaBeta alphaBeta;
    private final MoveSorterInteriorBuilder moveSorterBuilder;
    private AlphaBetaInteriorNodeVisited alphaBetaNodeStatistics;
    private TranspositionTableQ transpositionTableQ;
    private ZobristTracker zobristQTracker;
    private DebugFilter debugFilter;
    private UpdatePV updatePV;
    private boolean withStatistics;
    private boolean withZobristTracker;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;


    public CheckResolverChainBuilder() {
        alphaBeta = new AlphaBeta();
        moveSorterBuilder = new MoveSorterInteriorBuilder();
    }

    public CheckResolverChainBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
        this.moveSorterBuilder.withSmartListenerMediator(searchListenerMediator);
        return this;
    }

    public CheckResolverChainBuilder withStatistics() {
        this.withStatistics = true;
        return this;
    }

    public CheckResolverChainBuilder withTranspositionTable() {
        this.withTranspositionTable = true;
        return this;
    }

    public CheckResolverChainBuilder withTranspositionMoveSorter() {
        if (!withTranspositionTable) {
            throw new RuntimeException("You must enable QTranspositionTable first");
        }
        moveSorterBuilder.withTranspositionTable();
        return this;
    }

    public CheckResolverChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

    public CheckResolverChainBuilder withDebugSearchTree() {
        moveSorterBuilder.withDebugSearchTree();
        this.withDebugSearchTree = true;
        return this;
    }

    @Override
    protected  void buildObjects() {
        if (withStatistics) {
            alphaBetaNodeStatistics = new AlphaBetaInteriorNodeVisited();
        }
        if (withZobristTracker) {
            zobristQTracker = new ZobristTracker();
        }
        if (withTranspositionTable) {
            transpositionTableQ = new TranspositionTableQ();
        }
        if (withDebugSearchTree) {
            debugFilter = new DebugFilter(DebugNode.NodeTopology.CHECK_EXTENSION);
        }
        if (!withTranspositionTable) {
            updatePV = new UpdatePV();
        }
    }

    @Override
    protected  void setupListenerMediator() {
        if (withStatistics) {
            searchListenerMediator.add(alphaBetaNodeStatistics);
        }
        if (zobristQTracker != null) {
            searchListenerMediator.add(zobristQTracker);
        }
        if (transpositionTableQ != null) {
            searchListenerMediator.add(transpositionTableQ);
        }
        if (debugFilter != null) {
            searchListenerMediator.add(debugFilter);
        }
        if (updatePV != null) {
            searchListenerMediator.add(updatePV);
        }
        searchListenerMediator.add(alphaBeta);
    }

    @Override
    protected void linkObjects() {
        alphaBeta.setMoveSorter(moveSorterBuilder.build());
    }

    @Override
    protected AlphaBetaFilter buildAlphaBetaChain() {
        List<AlphaBetaFilter> chain = new LinkedList<>();

        if (debugFilter != null) {
            chain.add(debugFilter);
        }

        if (zobristQTracker != null) {
            chain.add(zobristQTracker);
        }

        if (transpositionTableQ != null) {
            chain.add(transpositionTableQ);
        }

        if (alphaBetaNodeStatistics != null) {
            chain.add(alphaBetaNodeStatistics);
        }

        chain.add(alphaBeta);

        if (updatePV != null) {
            chain.add(updatePV);
        }

        //chain.save(extensionFlowControl);


        return createChain(chain);
    }
}
