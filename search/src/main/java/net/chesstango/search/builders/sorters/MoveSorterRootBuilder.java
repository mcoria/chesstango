package net.chesstango.search.builders.sorters;

import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.sorters.*;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;
import net.chesstango.search.smart.sorters.groupsorters.CatchAllGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveSorterRootBuilder extends AbstractMoveSorterBuilder {
    private final RootMoveSorter rootMoveSorter;
    private final NodeGroupSorter nodeGroupSorter;
    private MoveSorterDebug moveSorterDebug;

    private SearchListenerMediator searchListenerMediator;

    private boolean withDebugSearchTree;

    public MoveSorterRootBuilder() {
        rootMoveSorter = new RootMoveSorter();
        nodeGroupSorter = new NodeGroupSorter();
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
        nodeGroupSorter.setGroupSorter(new CatchAllGroup());

        if (withDebugSearchTree) {
            moveSorterDebug = new MoveSorterDebug();
        }
    }

    private void setupListenerMediator() {
        if (moveSorterDebug != null) {
            searchListenerMediator.add(moveSorterDebug);
        }

        searchListenerMediator.add(rootMoveSorter);

        searchListenerMediator.add(nodeGroupSorter);
    }

    private MoveSorter createChain() {
        List<MoveSorter> chain = new LinkedList<>();

        if (moveSorterDebug != null) {
            chain.add(moveSorterDebug);
        }

        chain.add(rootMoveSorter);

        chain.add(nodeGroupSorter);

        return buildChain(chain);
    }

}
