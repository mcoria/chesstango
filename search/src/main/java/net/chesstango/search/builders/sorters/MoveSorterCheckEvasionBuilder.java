package net.chesstango.search.builders.sorters;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.alphabeta.pv.groupsorters.PrincipalVariationGroup;
import net.chesstango.search.alphabeta.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.alphabeta.transposition.groupsorters.TranspositionHeadMoveGroup;
import net.chesstango.search.alphabeta.transposition.groupsorters.TranspositionTailMoveGroup;
import net.chesstango.search.sorters.*;
import net.chesstango.search.sorters.comparators.DefaultMoveComparator;
import net.chesstango.search.sorters.groupsorters.AgregateGroupSorter;
import net.chesstango.search.sorters.groupsorters.CatchAllGroupSorter;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveSorterCheckEvasionBuilder extends AbstractMoveSorterBuilder {
    private final DefaultMoveComparator defaultMoveComparator;
    private final NodeMoveSorter nodeMoveSorter;
    private MoveSorterDebug moveSorterDebug;

    private PrincipalVariationGroup principalVariationGroup;
    private TranspositionHeadMoveGroup transpositionHeadMoveGroup;
    private TranspositionTailMoveGroup transpositionTailMoveGroup;
    private TranspositionTailMoveComparator transpositionTailMoveComparator;

    private boolean withIterativeDeepening;
    private boolean withTranspositionTable;
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

    public MoveSorterCheckEvasionBuilder withTranspositionTable() {
        this.withTranspositionTable = true;
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

        if (withTranspositionTable) {
            transpositionHeadMoveGroup = new TranspositionHeadMoveGroup();

            transpositionTailMoveGroup = new TranspositionTailMoveGroup();
            transpositionTailMoveComparator = new TranspositionTailMoveComparator();
        }
    }

    @Override
    protected void setupListeners() {
        listenerMediator.add(nodeMoveSorter);

        if (moveSorterDebug != null) {
            listenerMediator.add(moveSorterDebug);
        }

        if (principalVariationGroup != null) {
            listenerMediator.add(principalVariationGroup);
            nodeMoveSorter.addSortListener(principalVariationGroup);
        }

        if (transpositionHeadMoveGroup != null) {
            listenerMediator.add(transpositionHeadMoveGroup);
            nodeMoveSorter.addSortListener(transpositionHeadMoveGroup);
        }

        if (transpositionTailMoveGroup != null) {
            listenerMediator.add(transpositionTailMoveGroup);
        }

        if (transpositionTailMoveComparator != null) {
            listenerMediator.add(transpositionTailMoveComparator);
        }
    }

    @Override
    protected void link() {
        nodeMoveSorter.setGroupSorter(createGroupSorterChain());

        MoveToHashMap moveToZobrist = new MoveToHashMap();

        nodeMoveSorter.setMoveToZobrist(moveToZobrist);

        if (transpositionTailMoveGroup != null && transpositionTailMoveComparator != null) {
            transpositionTailMoveGroup.setMoveToZobrist(moveToZobrist);
            transpositionTailMoveComparator.setMoveToZobrist(moveToZobrist);

            Comparator<Move> moveComparator = transpositionTailMoveComparator
                    .thenComparing(new DefaultMoveComparator());

            transpositionTailMoveGroup.setMoveComparator(moveComparator.reversed());
        }
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

    private GroupSorter createGroupSorterChain() {
        AgregateGroupSorter agregateGroupSorter = new AgregateGroupSorter();

        if (principalVariationGroup != null) {
            agregateGroupSorter.addGroupSorter(principalVariationGroup);
        }

        if (transpositionHeadMoveGroup != null) {
            agregateGroupSorter.addGroupSorter(transpositionHeadMoveGroup);
        }

        if (transpositionTailMoveGroup != null) {
            agregateGroupSorter.addGroupSorter(transpositionTailMoveGroup);
        }

        agregateGroupSorter.addGroupSorter(new CatchAllGroupSorter());

        return agregateGroupSorter;
    }

    private MoveComparator createComparatorChain() {
        List<MoveComparator> chain = new LinkedList<>();

        chain.add(defaultMoveComparator);

        return linkMoveComparatorChain(chain);
    }
}
