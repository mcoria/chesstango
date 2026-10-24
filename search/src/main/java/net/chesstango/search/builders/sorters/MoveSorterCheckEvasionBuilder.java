package net.chesstango.search.builders.sorters;

import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.alphabeta.pv.sorters.PrincipalVariationPredicate;
import net.chesstango.search.alphabeta.transposition.sorters.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.alphabeta.transposition.sorters.TranspositionHeadPredicate;
import net.chesstango.search.alphabeta.transposition.sorters.TranspositionTailPredicate;
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

    private PrincipalVariationPredicate principalVariationPredicate;
    private TranspositionHeadPredicate transpositionHeadPredicate;
    private TranspositionTailPredicate transpositionTailPredicate;

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
            principalVariationPredicate = new PrincipalVariationPredicate();
        }

        if (withTranspositionTable) {
            transpositionHeadPredicate = new TranspositionHeadPredicate();

            transpositionTailPredicate = new TranspositionTailPredicate();
            transpositionTailMoveComparator = new TranspositionTailMoveComparator();
        }
    }

    @Override
    protected void setupListeners() {
        listenerMediator.add(nodeMoveSorter);

        if (moveSorterDebug != null) {
            listenerMediator.add(moveSorterDebug);
        }
        if (transpositionHeadPredicate != null) {
            listenerMediator.add(transpositionHeadPredicate);
        }

        if (transpositionTailPredicate != null) {
            listenerMediator.add(transpositionTailPredicate);
        }

        if (transpositionTailMoveComparator != null) {
            listenerMediator.add(transpositionTailMoveComparator);
        }

        if (principalVariationPredicate != null) {
            listenerMediator.add(principalVariationPredicate);
        }

    }

    @Override
    protected void link() {
        MoveToHashMap moveToZobrist = new MoveToHashMap();

        if (transpositionTailPredicate != null) {
            transpositionTailPredicate.setMoveToZobrist(moveToZobrist);
        }

        if (transpositionTailMoveComparator != null) {
            transpositionTailMoveComparator.setMoveToZobrist(moveToZobrist);
        }

        nodeMoveSorter.setMoveToZobrist(moveToZobrist);
        nodeMoveSorter.setSorterListeners(createSortListeners());
        nodeMoveSorter.setMoveSorterStages(createGroupSorterChain());
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

    private List<MoveSorterListener> createSortListeners() {
        List<MoveSorterListener> moveSorterListeners = new ArrayList<>();

        if (transpositionHeadPredicate != null) {
            moveSorterListeners.add(transpositionHeadPredicate);
        }

        if (principalVariationPredicate != null) {
            moveSorterListeners.add(principalVariationPredicate);
        }

        return moveSorterListeners;
    }


    private List<NodeMoveSorterStage> createGroupSorterChain() {
        List<NodeMoveSorterStage> nodeMoveSorterStages = new ArrayList<>();

        if (principalVariationPredicate != null) {
            nodeMoveSorterStages.add(NodeMoveSorterStage.createPrincipalVariationStage(principalVariationPredicate));
        }

        if (transpositionHeadPredicate != null) {
            nodeMoveSorterStages.add(NodeMoveSorterStage.createTranspositionHeadStage(transpositionHeadPredicate));
        }

        if (transpositionTailPredicate != null && transpositionTailMoveComparator!=null) {
            nodeMoveSorterStages.add(NodeMoveSorterStage.createTranspositionTailStage(transpositionTailPredicate, transpositionTailMoveComparator));
        }

        nodeMoveSorterStages.add(NodeMoveSorterStage.createCatchAllStage());

        return nodeMoveSorterStages;
    }
}
