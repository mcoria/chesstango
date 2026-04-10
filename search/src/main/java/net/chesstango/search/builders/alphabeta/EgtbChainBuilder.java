package net.chesstango.search.builders.alphabeta;

import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.egtb.filters.EgtbEvaluation;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.AlphaBetaEgtbNodeStatistics;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class EgtbChainBuilder extends AbstractChainBuilder {
    private final EgtbEvaluation egtbEvaluation;

    private DebugFilter debugFilter;
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

    public EgtbChainBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
        return this;
    }

    @Override
    protected  void buildObjects() {
        if (withDebugSearchTree) {
            this.debugFilter = new DebugFilter(DebugNode.NodeTopology.EGTB);
        }

        if (withStatistics) {
            alphaBetaEgtbNodeStatistics = new AlphaBetaEgtbNodeStatistics();
        }
    }

    @Override
    protected  void setupListenerMediator() {
        if (debugFilter != null) {
            searchListenerMediator.add(debugFilter);
        }

        if (alphaBetaEgtbNodeStatistics != null) {
            searchListenerMediator.add(alphaBetaEgtbNodeStatistics);
        }

        searchListenerMediator.add(egtbEvaluation);
    }

    @Override
    protected void linkObjects() {

    }

    @Override
    protected AlphaBetaFilter buildAlphaBetaChain() {
        List<AlphaBetaFilter> chain = new LinkedList<>();

        if (debugFilter != null) {
            chain.add(debugFilter);
        }

        if (alphaBetaEgtbNodeStatistics != null) {
            chain.add(alphaBetaEgtbNodeStatistics);
        }

        chain.add(egtbEvaluation);

        return createChain(chain);
    }
}
