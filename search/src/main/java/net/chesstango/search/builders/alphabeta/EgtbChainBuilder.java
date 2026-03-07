package net.chesstango.search.builders.alphabeta;

import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.egtb.filters.EgtbEvaluation;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class EgtbChainBuilder extends AbstractChainBuilder {
    private final EgtbEvaluation egtbEvaluation;

    private SearchListenerMediator searchListenerMediator;
    private DebugFilter debugFilter;

    private boolean withDebugSearchTree;

    public EgtbChainBuilder() {
        egtbEvaluation = new EgtbEvaluation();
    }

    public EgtbChainBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
        return this;
    }

    public EgtbChainBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
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
        if (withDebugSearchTree) {
            this.debugFilter = new DebugFilter(DebugNode.NodeTopology.EGTB);
        }
    }

    private void setupListenerMediator() {
        if (debugFilter != null) {
            searchListenerMediator.add(debugFilter);
        }

        searchListenerMediator.add(egtbEvaluation);
    }

    private AlphaBetaFilter createChain() {
        List<AlphaBetaFilter> chain = new LinkedList<>();

        if (debugFilter != null) {
            chain.add(debugFilter);
        }

        chain.add(egtbEvaluation);

        return createChain(chain);
    }
}
