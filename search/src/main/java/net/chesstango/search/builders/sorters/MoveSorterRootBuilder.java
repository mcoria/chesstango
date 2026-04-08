package net.chesstango.search.builders.sorters;

import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.MoveSorterDebug;
import net.chesstango.search.smart.sorters.NodeGroupSorter;
import net.chesstango.search.smart.sorters.RootMoveSorter;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;
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
    private final DefaultMoveComparator defaultMoveComparator;

    private MoveSorterDebug moveSorterDebug;

    private boolean withIterativeDeepening;
    private boolean withDebugSearchTree;

    public MoveSorterRootBuilder() {
        this.rootMoveSorter = new RootMoveSorter();
        this.nodeGroupSorter = new NodeGroupSorter();
        this.catchAllSortGroup = new CatchAllSortGroup();
        this.defaultMoveComparator = new DefaultMoveComparator();
    }

    @Override
    public MoveSorterBuilder withIterativeDeepening() {
        this.withIterativeDeepening = true;
        return null;
    }

    @Override
    public MoveSorterRootBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    @Override
    protected void buildObjects() {
        if (withDebugSearchTree) {
            moveSorterDebug = new MoveSorterDebug();
        }
    }

    @Override
    protected void setupListenerMediator() {
        if (moveSorterDebug != null) {
            searchListenerMediator.add(moveSorterDebug);
        }

        searchListenerMediator.add(rootMoveSorter);

        searchListenerMediator.add(nodeGroupSorter);
    }

    @Override
    protected void linkObjects() {
        catchAllSortGroup.setMoveComparator(defaultMoveComparator);
        nodeGroupSorter.setGroupSorter(catchAllSortGroup);
    }

    @Override
    protected MoveSorter buildSorterChain() {
        List<MoveSorter> chain = new LinkedList<>();

        if (moveSorterDebug != null) {
            chain.add(moveSorterDebug);
        }

        chain.add(rootMoveSorter);

        chain.add(nodeGroupSorter);

        return linkMoveSorterChain(chain);
    }

}
