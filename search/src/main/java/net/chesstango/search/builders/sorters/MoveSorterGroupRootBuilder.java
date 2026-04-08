package net.chesstango.search.builders.sorters;

import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.pv.groupsorters.PrincipalVariationGroup;
import net.chesstango.search.smart.sorters.*;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;
import net.chesstango.search.smart.sorters.groupsorters.CatchAllSortGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveSorterGroupRootBuilder extends AbstractMoveSorterBuilder {
    private final RootMoveSorter rootMoveSorter;
    private final NodeGroupSorter nodeGroupSorter;
    private final CatchAllSortGroup catchAllSortGroup;
    private final DefaultMoveComparator defaultMoveComparator;

    private MoveSorterDebug moveSorterDebug;

    private PrincipalVariationGroup principalVariationGroup;

    private SearchListenerMediator searchListenerMediator;

    private boolean withIterativeDeepening;
    private boolean withDebugSearchTree;

    public MoveSorterGroupRootBuilder() {
        this.rootMoveSorter = new RootMoveSorter();
        this.nodeGroupSorter = new NodeGroupSorter();
        this.catchAllSortGroup = new CatchAllSortGroup();
        this.defaultMoveComparator = new DefaultMoveComparator();
    }

    public MoveSorterGroupRootBuilder withIterativeDeepening() {
        this.withIterativeDeepening = true;
        return this;
    }

    public MoveSorterGroupRootBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    public MoveSorterGroupRootBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
        return this;
    }

    @Override
    protected void buildObjects() {
        if (withDebugSearchTree) {
            moveSorterDebug = new MoveSorterDebug();
        }

        if (withIterativeDeepening) {
            principalVariationGroup = new PrincipalVariationGroup();
        }
    }

    @Override
    protected void setupListenerMediator() {
        searchListenerMediator.add(rootMoveSorter);

        searchListenerMediator.add(nodeGroupSorter);

        if (moveSorterDebug != null) {
            searchListenerMediator.add(moveSorterDebug);
        }

        if (principalVariationGroup != null) {
            searchListenerMediator.add(principalVariationGroup);
        }
    }

    @Override
    protected void linkObjects() {
        catchAllSortGroup.setMoveComparator(defaultMoveComparator);
        nodeGroupSorter.setGroupSorter(createGroupSorterChain());
    }

    @Override
    protected MoveSorter buildSorterChain() {
        List<MoveSorter> chain = new LinkedList<>();

        if (moveSorterDebug != null) {
            chain.add(moveSorterDebug);
        }

        chain.add(nodeGroupSorter);

        return linkMoveSorterChain(chain);
    }

    private GroupSorter createGroupSorterChain() {
        GroupSorter head = null;

        if (principalVariationGroup != null) {
            principalVariationGroup.setNext(catchAllSortGroup);
            head = principalVariationGroup;
        }
        if (head == null) {
            head = catchAllSortGroup;
        }

        return head;
    }

}
