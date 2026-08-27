package net.chesstango.search.builders.alphabeta;

import net.chesstango.search.ListenerMediator;
import net.chesstango.search.smart.AlphaBetaFilter;
import net.chesstango.search.smart.debug.filters.DebugFilter;
import net.chesstango.search.smart.debug.model.NodeTopology;
import net.chesstango.search.smart.egtb.filters.EgtbEvaluation;
import net.chesstango.search.smart.pv.filters.ExtendPV;
import net.chesstango.search.smart.statistics.node.filters.AlphaBetaEgtbNodeStatistics;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class EgtbChainBuilder extends AbstractChainBuilder {
    private final EgtbEvaluation egtbEvaluation;

    private DebugFilter debugFilter;
    private ExtendPV extendPV;
    private AlphaBetaEgtbNodeStatistics alphaBetaEgtbNodeStatistics;

    private boolean withDebugSearchTree;
    private boolean withStatistics;

    public EgtbChainBuilder() {
        egtbEvaluation = new EgtbEvaluation();
    }

    public EgtbChainBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    public EgtbChainBuilder withStatistics() {
        this.withStatistics = true;
        return this;
    }

    public EgtbChainBuilder withSmartListenerMediator(ListenerMediator listenerMediator) {
        this.listenerMediator = listenerMediator;
        return this;
    }

    @Override
    protected  void buildObjects() {
        extendPV = new ExtendPV();

        if (withDebugSearchTree) {
            debugFilter = new DebugFilter(NodeTopology.EGTB);
        }

        if (withStatistics) {
            alphaBetaEgtbNodeStatistics = new AlphaBetaEgtbNodeStatistics();
        }
    }

    @Override
    protected  void setupListenerMediator() {
        if (debugFilter != null) {
            listenerMediator.add(debugFilter);
        }

        if (extendPV != null) {
            listenerMediator.add(extendPV);
        }

        if (alphaBetaEgtbNodeStatistics != null) {
            listenerMediator.add(alphaBetaEgtbNodeStatistics);
        }

        listenerMediator.add(egtbEvaluation);
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

        if (alphaBetaEgtbNodeStatistics != null) {
            chain.add(alphaBetaEgtbNodeStatistics);
        }

        chain.add(egtbEvaluation);

        return createChain(chain);
    }
}
