package net.chesstango.search.builders;

import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.debug.DebugSorter;
import net.chesstango.search.smart.sorters.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveSorterBuilder {
    private MoveSorterStart moveSorterStart;
    private SmartListenerMediator smartListenerMediator;
    private TranspositionMoveSorter transpositionMoveSorter;
    private DefaultMoveSorterElement defaultMoveSorterElement;
    private DebugSorter debugSorter;

    private boolean withTranspositionMoveSorter;
    private boolean withQTranspositionMoveSorter;
    private boolean withDebugSearchTree;

    public void withSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        this.smartListenerMediator = smartListenerMediator;
    }

    public void withQTranspositionMoveSorter() {
        this.withQTranspositionMoveSorter = true;
    }

    public void withTranspositionMoveSorter() {
        this.withTranspositionMoveSorter = true;
    }

    public void withDebugSearchTree() {
        this.withDebugSearchTree = true;
    }

    public MoveSorter build() {
        if (withTranspositionMoveSorter && withQTranspositionMoveSorter) {
            throw new RuntimeException("TranspositionMoveSorter and QTranspositionMoveSorter are mutual exclusive");
        }

        buildObjects();

        setupListenerMediator();

        moveSorterStart.setNextMoveSorter(createChain());

        MoveSorter sorter = moveSorterStart;

        if (debugSorter != null) {
            sorter = debugSorter;

        }

        return sorter;
    }

    private void buildObjects() {
        if (withTranspositionMoveSorter) {
            moveSorterStart = new MoveSorterStart();
            transpositionMoveSorter = new TranspositionMoveSorter(SearchByCycleContext::getMaxMap, SearchByCycleContext::getMinMap);
        } else if (withQTranspositionMoveSorter) {
            moveSorterStart = new MoveSorterStart(move -> !move.isQuiet());
            transpositionMoveSorter = new TranspositionMoveSorter(SearchByCycleContext::getQMaxMap, SearchByCycleContext::getQMinMap);
        } else {
            moveSorterStart = new MoveSorterStart();
        }

        if (withDebugSearchTree) {
            debugSorter = new DebugSorter();
            debugSorter.setMoveSorterImp(moveSorterStart);
        }

        defaultMoveSorterElement = new DefaultMoveSorterElement();
    }

    private void setupListenerMediator() {
        smartListenerMediator.add(moveSorterStart);

        if (transpositionMoveSorter != null) {
            smartListenerMediator.add(transpositionMoveSorter);
        }

        if (debugSorter != null) {
            smartListenerMediator.add(debugSorter);
        }

    }

    private MoveSorterElement createChain() {
        List<MoveSorterElement> chain = new LinkedList<>();

        if (transpositionMoveSorter != null) {
            chain.add(transpositionMoveSorter);
        }

        chain.add(defaultMoveSorterElement);

        for (int i = 0; i < chain.size() - 1; i++) {
            MoveSorterElement currentSorter = chain.get(i);
            MoveSorterElement next = chain.get(i + 1);

            if (currentSorter instanceof TranspositionMoveSorter) {
                transpositionMoveSorter.setNext(next);
            } else {
                throw new RuntimeException("filter not found");
            }
        }

        return chain.get(0);
    }

}
