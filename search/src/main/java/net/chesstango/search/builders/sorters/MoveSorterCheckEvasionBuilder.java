package net.chesstango.search.builders.sorters;

import net.chesstango.search.alphabeta.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.sorters.MoveComparator;
import net.chesstango.search.sorters.MoveSorter;
import net.chesstango.search.sorters.MoveSorterDebug;
import net.chesstango.search.sorters.NodeMoveSorter;
import net.chesstango.search.sorters.comparators.DefaultMoveComparator;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveSorterCheckEvasionBuilder extends AbstractMoveSorterBuilder {
    private final DefaultMoveComparator defaultMoveComparator;
    private final NodeMoveSorter nodeMoveSorter;
    private MoveSorterDebug moveSorterDebug;

    private PrincipalVariationComparator principalVariationComparator;

    private boolean withIterativeDeepening;
    private boolean withDebugSearchTree;

    public MoveSorterCheckEvasionBuilder() {
        this.nodeMoveSorter = new NodeMoveSorter();
        this.defaultMoveComparator = new DefaultMoveComparator();
    }

    @Override
    public MoveSorterCheckEvasionBuilder withIterativeDeepening() {
        this.withIterativeDeepening = true;
        return this;
    }

    @Override
    public MoveSorterCheckEvasionBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    @Override
    protected void buildObjects() {
        if (withIterativeDeepening) {
            principalVariationComparator = new PrincipalVariationComparator();
        }

        if (withDebugSearchTree) {
            moveSorterDebug = new MoveSorterDebug();
        }
    }

    @Override
    protected void setupListeners() {
        listenerMediator.add(nodeMoveSorter);

        if (principalVariationComparator != null) {
            listenerMediator.add(principalVariationComparator);
            nodeMoveSorter.addSortListener(principalVariationComparator);
        }

        if (moveSorterDebug != null) {
            listenerMediator.add(moveSorterDebug);
        }
    }

    @Override
    protected void link() {
        nodeMoveSorter.setMoveComparator(createComparatorChain());
    }

    @Override
    protected MoveSorter buildSorterChain() {
        List<MoveSorter> chain = new LinkedList<>();

        if (moveSorterDebug != null) {
            chain.add(moveSorterDebug);
        }

        chain.add(nodeMoveSorter);

        return linkMoveSorterChain(chain);
    }


    private MoveComparator createComparatorChain() {
        List<MoveComparator> chain = new LinkedList<>();

        if (principalVariationComparator != null) {
            chain.add(principalVariationComparator);
        }

        chain.add(defaultMoveComparator);

        return linkMoveComparatorChain(chain);
    }
}
