package net.chesstango.search.builders.alphabeta;


import net.chesstango.search.ListenerMediator;
import net.chesstango.search.alphabeta.AlphaBetaFilter;
import net.chesstango.search.alphabeta.core.filters.AlphaBeta;
import net.chesstango.search.alphabeta.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.alphabeta.debug.model.NodeTopology;
import net.chesstango.search.alphabeta.pv.filters.ExtendPV;
import net.chesstango.search.alphabeta.pv.filters.PropagatePV;
import net.chesstango.search.alphabeta.statistics.node.filters.CheckEvasionNodeExpected;
import net.chesstango.search.alphabeta.statistics.node.filters.CheckEvasionNodeVisited;
import net.chesstango.search.builders.sorters.MoveSorterCheckEvasionBuilder;
import net.chesstango.search.sorters.MoveSorter;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class CheckEvasionChainBuilder extends AbstractChainBuilder {
    private final MoveSorterCheckEvasionBuilder moveSorterCheckEvasionBuilder;
    private final AlphaBeta alphaBeta;
    private AlphaBetaFlowControl alphaBetaFlowControl;
    private DebugFilter debugFilter;
    private ExtendPV extendPV;
    private PropagatePV propagatePV;
    private MoveSorter moveSorter;

    /**
     * Statistics
     */
    private CheckEvasionNodeVisited checkEvasionNodeVisited;
    private CheckEvasionNodeExpected checkEvasionNodeExpected;


    private boolean withDebugSearchTree;
    private boolean withStatistics;

    public CheckEvasionChainBuilder() {
        alphaBeta = new AlphaBeta();
        moveSorterCheckEvasionBuilder = new MoveSorterCheckEvasionBuilder();
    }

    public CheckEvasionChainBuilder withIterativeDeepening() {
        moveSorterCheckEvasionBuilder.withIterativeDeepening();
        return this;
    }

    public CheckEvasionChainBuilder withAlphaBetaFlowControl(AlphaBetaFlowControl alphaBetaFlowControl) {
        this.alphaBetaFlowControl = alphaBetaFlowControl;
        return this;
    }

    public CheckEvasionChainBuilder withSmartListenerMediator(ListenerMediator listenerMediator) {
        moveSorterCheckEvasionBuilder.withSmartListenerMediator(listenerMediator);
        this.listenerMediator = listenerMediator;
        return this;
    }

    public CheckEvasionChainBuilder withStatistics() {
        this.withStatistics = true;
        return this;
    }

    public CheckEvasionChainBuilder withDebugSearchTree() {
        moveSorterCheckEvasionBuilder.withDebugSearchTree();
        this.withDebugSearchTree = true;
        return this;
    }


    @Override
    protected void buildObjects() {
        extendPV = new ExtendPV();
        propagatePV = new PropagatePV();

        if (withStatistics) {
            checkEvasionNodeVisited = new CheckEvasionNodeVisited();
            checkEvasionNodeExpected = new CheckEvasionNodeExpected();
        }

        if (withDebugSearchTree) {
            debugFilter = new DebugFilter(NodeTopology.CHECK_EVASION);
        }

        moveSorter = moveSorterCheckEvasionBuilder.build();
    }

    @Override
    protected void setupListenerMediator() {
        listenerMediator.add(alphaBeta);

        if (checkEvasionNodeVisited != null) {
            listenerMediator.add(checkEvasionNodeVisited);
        }

        if (checkEvasionNodeExpected != null) {
            listenerMediator.add(checkEvasionNodeExpected);
        }
        if (debugFilter != null) {
            listenerMediator.add(debugFilter);
        }

        if (extendPV != null) {
            listenerMediator.add(extendPV);
        }

        if (propagatePV != null) {
            listenerMediator.add(propagatePV);
        }
    }

    @Override
    public void link() {
        alphaBeta.setMoveSorter(moveSorter);
    }

    @Override
    protected AlphaBetaFilter buildAlphaBetaChain() {
        List<AlphaBetaFilter> chain = new LinkedList<>();

        if (debugFilter != null) {
            chain.add(debugFilter);
        }

        if (extendPV != null) {
            chain.add(extendPV);
        }

        if (checkEvasionNodeVisited != null) {
            chain.add(checkEvasionNodeVisited);
        }

        // Debe ir despues de TT para que contabilice expected correctamente
        if (checkEvasionNodeExpected != null) {
            chain.add(checkEvasionNodeExpected);
        }

        chain.add(alphaBeta);

        if (propagatePV != null) {
            chain.add(propagatePV);
        }

        chain.add(alphaBetaFlowControl);

        return createChain(chain);
    }
}
