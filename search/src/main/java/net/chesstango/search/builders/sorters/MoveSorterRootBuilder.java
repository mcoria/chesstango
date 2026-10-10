package net.chesstango.search.builders.sorters;

import net.chesstango.search.sorters.MoveSorter;
import net.chesstango.search.sorters.MoveSorterDebug;
import net.chesstango.search.sorters.NodeGroupSorter;
import net.chesstango.search.sorters.RootMoveSorter;
import net.chesstango.search.sorters.comparators.DefaultMoveComparator;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveSorterRootBuilder extends AbstractMoveSorterBuilder {
    private final RootMoveSorter rootMoveSorter;
    private final DefaultMoveComparator defaultMoveComparator;

    private MoveSorterDebug moveSorterDebug;

    private boolean withIterativeDeepening;
    private boolean withDebugSearchTree;

    public MoveSorterRootBuilder() {
        this.rootMoveSorter = new RootMoveSorter();
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
    protected void setupListeners() {
        if (moveSorterDebug != null) {
            listenerMediator.add(moveSorterDebug);
        }

        listenerMediator.add(rootMoveSorter);
    }

    @Override
    protected void link() {
    }

    @Override
    protected MoveSorter buildSorterChain() {
        List<MoveSorter> chain = new LinkedList<>();

        if (moveSorterDebug != null) {
            chain.add(moveSorterDebug);
        }

        chain.add(rootMoveSorter);

        return linkMoveSorterChain(chain);
    }

}
