package net.chesstango.search.builders;

import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.debug.DebugSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.NodeMoveSorter;
import net.chesstango.search.smart.sorters.comparators.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveSorterBuilder {
    private SmartListenerMediator smartListenerMediator;
    private NodeMoveSorter nodeMoveSorter;
    private DefaultMoveComparator defaultMoveComparator;
    private RecaptureMoveComparator recaptureMoveComparator;
    private TranspositionHeadMoveComparator transpositionHeadMoveComparator;
    private TranspositionTailMoveComparator transpositionTailMoveComparator;
    private DebugSorter debugSorter;
    private boolean withQuietFilter;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;

    public void withSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        this.smartListenerMediator = smartListenerMediator;
    }

    public void withMoveQuietFilter() {
        this.withQuietFilter = true;
    }

    public void withTranspositionTable() {
        this.withTranspositionTable = true;
    }

    public void withDebugSearchTree() {
        this.withDebugSearchTree = true;
    }


    public MoveSorter build() {
        buildObjects();

        setupListenerMediator();


        MoveSorter moveSorter = nodeMoveSorter;
        nodeMoveSorter.setMoveComparator(createComparatorChain());

        if (debugSorter != null) {
            debugSorter.setMoveSorterImp(moveSorter);
            moveSorter = debugSorter;
        }

        return moveSorter;
    }

    private void buildObjects() {
        nodeMoveSorter = withQuietFilter ? new NodeMoveSorter(move -> !move.isQuiet()) : new NodeMoveSorter();

        recaptureMoveComparator = new RecaptureMoveComparator();

        defaultMoveComparator = new DefaultMoveComparator();

        if (withTranspositionTable) {
            if (withQuietFilter) {
                transpositionHeadMoveComparator = new TranspositionHeadMoveComparator(SearchByCycleContext::getQMaxMap, SearchByCycleContext::getQMinMap);
                transpositionTailMoveComparator = new TranspositionTailMoveComparator(SearchByCycleContext::getQMaxMap, SearchByCycleContext::getQMinMap);
            } else {
                transpositionHeadMoveComparator = new TranspositionHeadMoveComparator(SearchByCycleContext::getMaxMap, SearchByCycleContext::getMinMap);
                transpositionTailMoveComparator = new TranspositionTailMoveComparator(SearchByCycleContext::getMaxMap, SearchByCycleContext::getMinMap);
            }
        }

        if (withDebugSearchTree) {
            debugSorter = new DebugSorter();
        }
    }

    private void setupListenerMediator() {
        if (nodeMoveSorter != null) {
            smartListenerMediator.add(nodeMoveSorter);
        }

        if (transpositionHeadMoveComparator != null) {
            smartListenerMediator.add(transpositionHeadMoveComparator);
        }

        if (transpositionTailMoveComparator != null) {
            smartListenerMediator.add(transpositionTailMoveComparator);
        }

        if (recaptureMoveComparator != null) {
            smartListenerMediator.add(recaptureMoveComparator);
        }

        if (debugSorter != null) {
            smartListenerMediator.add(debugSorter);
        }
    }


    private MoveComparator createComparatorChain() {
        List<MoveComparator> chain = new LinkedList<>();

        if (withTranspositionTable) {
            chain.add(transpositionHeadMoveComparator);
            chain.add(transpositionTailMoveComparator);
        }

        chain.add(recaptureMoveComparator);

        chain.add(defaultMoveComparator);

        for (int i = 0; i < chain.size() - 1; i++) {
            MoveComparator currentComparator = chain.get(i);
            MoveComparator next = chain.get(i + 1);

            if (currentComparator instanceof TranspositionHeadMoveComparator) {
                transpositionHeadMoveComparator.setNext(next);
            } else if (currentComparator instanceof TranspositionTailMoveComparator) {
                transpositionTailMoveComparator.setNext(next);
            } else if (currentComparator instanceof RecaptureMoveComparator) {
                recaptureMoveComparator.setNext(next);
            } else {
                throw new RuntimeException("Unknow MoveComparator");
            }
        }


        return chain.get(0);
    }
}
