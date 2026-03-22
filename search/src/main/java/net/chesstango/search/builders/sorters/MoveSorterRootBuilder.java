package net.chesstango.search.builders.sorters;

import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.MoveSorterDebug;
import net.chesstango.search.smart.sorters.NodeMoveSorter;
import net.chesstango.search.smart.sorters.RootMoveSorter;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveSorterRootBuilder extends AbstractMoveSorterBuilder {
    private final RootMoveSorter rootMoveSorter;
    private final NodeMoveSorter nodeMoveSorter;
    private MoveSorterDebug moveSorterDebug;

    private SearchListenerMediator searchListenerMediator;

    private boolean withDebugSearchTree;

    public MoveSorterRootBuilder() {
        rootMoveSorter = new RootMoveSorter();
        nodeMoveSorter = new NodeMoveSorter();
    }

    public MoveSorterRootBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    public MoveSorterRootBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
        return this;
    }

    public MoveSorter build() {
        buildObjects();

        setupListenerMediator();

        return createChain();
    }

    private void buildObjects() {
        nodeMoveSorter.setMoveComparator(new DefaultMoveComparator());

        if (withDebugSearchTree) {
            moveSorterDebug = new MoveSorterDebug();
        }
    }

    private void setupListenerMediator() {
        if (moveSorterDebug != null) {
            searchListenerMediator.add(moveSorterDebug);
        }

        searchListenerMediator.add(nodeMoveSorter);

        searchListenerMediator.add(rootMoveSorter);
    }

    private MoveSorter createChain() {
        List<MoveSorter> chain = new LinkedList<>();

        if (moveSorterDebug != null) {
            chain.add(moveSorterDebug);
        }

        chain.add(rootMoveSorter);

        chain.add(nodeMoveSorter);

        return buildChain(chain);
    }

}
