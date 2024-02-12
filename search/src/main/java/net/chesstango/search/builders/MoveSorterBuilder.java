package net.chesstango.search.builders;

import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.debug.DebugSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.NodeMoveSorter;
import net.chesstango.search.smart.sorters.comparators.*;

/**
 * @author Mauricio Coria
 */
public class MoveSorterBuilder {
    private SmartListenerMediator smartListenerMediator;
    private NodeMoveSorter nodeMoveSorter;
    private DefaultMoveComparator defaultMoveComparator;
    private ComposedMoveComparator composedMoveComparator;
    private RecaptureMoveComparator recaptureMoveComparator;
    private TranspositionHeadMoveComparator transpositionHeadMoveComparator;
    private TranspositionTailMoveComparator transpositionTailMoveComparator;
    private DebugSorter debugSorter;
    private boolean withQuietFilter;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;
    private boolean withComposedMoveSorter;

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

    public void withComposedMoveSorter() {
        this.withComposedMoveSorter = true;
    }

    public MoveSorter build() {
        buildObjects();

        setupListenerMediator();

        return createChain();
    }

    private void buildObjects() {
        nodeMoveSorter = withQuietFilter ? new NodeMoveSorter(move -> !move.isQuiet()) : new NodeMoveSorter();

        if (withComposedMoveSorter) {
            composedMoveComparator = new ComposedMoveComparator();
            recaptureMoveComparator = new RecaptureMoveComparator();

            if (withTranspositionTable) {
                if (withQuietFilter) {
                    transpositionHeadMoveComparator = new TranspositionHeadMoveComparator(SearchByCycleContext::getQMaxMap, SearchByCycleContext::getQMinMap);
                    transpositionTailMoveComparator = new TranspositionTailMoveComparator(SearchByCycleContext::getQMaxMap, SearchByCycleContext::getQMinMap);
                } else {
                    transpositionHeadMoveComparator = new TranspositionHeadMoveComparator(SearchByCycleContext::getMaxMap, SearchByCycleContext::getMinMap);
                    transpositionTailMoveComparator = new TranspositionTailMoveComparator(SearchByCycleContext::getMaxMap, SearchByCycleContext::getMinMap);
                }
            }
        } else {
            defaultMoveComparator = new DefaultMoveComparator();
        }

        if (withDebugSearchTree) {
            debugSorter = new DebugSorter();
        }
    }

    private void setupListenerMediator() {
        if (nodeMoveSorter != null) {
            smartListenerMediator.add(nodeMoveSorter);
        }

        if (recaptureMoveComparator != null) {
            smartListenerMediator.add(recaptureMoveComparator);
        }

        if (transpositionHeadMoveComparator != null) {
            smartListenerMediator.add(transpositionHeadMoveComparator);
        }

        if (transpositionTailMoveComparator != null) {
            smartListenerMediator.add(transpositionTailMoveComparator);
        }

        if (debugSorter != null) {
            smartListenerMediator.add(debugSorter);
        }
    }

    private MoveSorter createChain() {
        MoveSorter result = nodeMoveSorter;

        if (withComposedMoveSorter) {

            if (withTranspositionTable) {
                composedMoveComparator.setTranspositionHeadMoveComparator(transpositionHeadMoveComparator);
                composedMoveComparator.setTranspositionTailMoveComparator(transpositionTailMoveComparator);
            }
            composedMoveComparator.setRecaptureMoveComparator(recaptureMoveComparator);

            nodeMoveSorter.setMoveComparator(composedMoveComparator);
        } else {
            nodeMoveSorter.setMoveComparator(defaultMoveComparator);
        }

        if (debugSorter != null) {
            debugSorter.setMoveSorterImp(result);
            result = debugSorter;
        }

        return result;
    }

}
