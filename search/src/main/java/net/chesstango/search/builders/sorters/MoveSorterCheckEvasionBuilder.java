package net.chesstango.search.builders.sorters;

import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.alphabeta.pv.groupsorters.PrincipalVariationGroup;
import net.chesstango.search.alphabeta.transposition.sorters.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.alphabeta.transposition.sorters.TranspositionHeadMoveGroup;
import net.chesstango.search.alphabeta.transposition.sorters.TranspositionTailMoveGroup;
import net.chesstango.search.sorters.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveSorterCheckEvasionBuilder extends AbstractMoveSorterBuilder {
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
        if (transpositionHeadMoveGroup != null) {
            listenerMediator.add(transpositionHeadMoveGroup);
        }

        if (transpositionTailMoveGroup != null) {
            listenerMediator.add(transpositionTailMoveGroup);
        }

        if (transpositionTailMoveComparator != null) {
            listenerMediator.add(transpositionTailMoveComparator);
        }

        if (principalVariationGroup != null) {
            listenerMediator.add(principalVariationGroup);
        }

    }

    @Override
    protected void link() {
        MoveToHashMap moveToZobrist = new MoveToHashMap();

        if (transpositionTailMoveGroup != null) {
            transpositionTailMoveGroup.setMoveToZobrist(moveToZobrist);
        }

        if (transpositionTailMoveComparator != null) {
            transpositionTailMoveComparator.setMoveToZobrist(moveToZobrist);
        }

        nodeMoveSorter.setMoveToZobrist(moveToZobrist);
        nodeMoveSorter.setSortListeners(createSortListeners());
        nodeMoveSorter.setNodeMoveSorterStages(createGroupSorterChain());
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

    private List<SortListener> createSortListeners() {
        List<SortListener> sortListeners = new ArrayList<>();

        if (transpositionHeadMoveGroup != null) {
            sortListeners.add(transpositionHeadMoveGroup);
        }

        if (principalVariationGroup != null) {
            sortListeners.add(principalVariationGroup);
        }

        return sortListeners;
    }


    private List<NodeMoveSorterStage> createGroupSorterChain() {
        List<NodeMoveSorterStage> nodeMoveSorterStages = new ArrayList<>();

        if (principalVariationGroup != null) {
            nodeMoveSorterStages.add(NodeMoveSorterStage.createPrincipalVariationStage(principalVariationGroup));
        }

        if (transpositionHeadMoveGroup != null) {
            nodeMoveSorterStages.add(NodeMoveSorterStage.createTranspositionHeadStage(transpositionHeadMoveGroup));
        }

        if (transpositionTailMoveGroup != null && transpositionTailMoveComparator!=null) {
            nodeMoveSorterStages.add(NodeMoveSorterStage.createTranspositionTailStage(transpositionTailMoveGroup, transpositionTailMoveComparator));
        }

        nodeMoveSorterStages.add(NodeMoveSorterStage.createCatchAllStage());

        return nodeMoveSorterStages;
    }
}
