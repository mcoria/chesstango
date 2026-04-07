package net.chesstango.search.builders.sorters;

import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.MoveSorterDebug;
import net.chesstango.search.smart.sorters.NodeGroupSorter;
import net.chesstango.search.smart.sorters.RootMoveSorter;
import net.chesstango.search.smart.sorters.groupsorters.CatchAllSortGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveSorterRootBuilder extends AbstractMoveSorterBuilder {
    private final RootMoveSorter rootMoveSorter;
    private final NodeGroupSorter nodeGroupSorter;
    private final CatchAllSortGroup catchAllSortGroup;


    private MoveSorterDebug moveSorterDebug;

    private SearchListenerMediator searchListenerMediator;

    private boolean withDebugSearchTree;

    public MoveSorterRootBuilder() {
        rootMoveSorter = new RootMoveSorter();
        nodeGroupSorter = new NodeGroupSorter();
        catchAllSortGroup = new CatchAllSortGroup();
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

        linkObjects();

        return buildSorterChain();
    }

    private void buildObjects() {
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

    private void linkObjects() {
        nodeGroupSorter.setGroupSorter(catchAllSortGroup);
    }

    private MoveSorter buildSorterChain() {
        List<MoveSorter> chain = new LinkedList<>();

        if (moveSorterDebug != null) {
            chain.add(moveSorterDebug);
        }

        chain.add(rootMoveSorter);

        chain.add(nodeGroupSorter);

        return linkMoveSorterChain(chain);
    }

}
