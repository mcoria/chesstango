package net.chesstango.search.builders.sorters;

import net.chesstango.search.ListenerMediator;
import net.chesstango.search.alphabeta.evalcache.comparators.EvaluatorCacheComparator;
import net.chesstango.search.alphabeta.killermoves.sorters.comparators.KillerMoveComparator;
import net.chesstango.search.sorters.*;
import net.chesstango.search.sorters.comparators.MoveComparator;
import net.chesstango.search.sorters.comparators.MvvLvaComparator;
import net.chesstango.search.sorters.comparators.PromotionComparator;
import net.chesstango.search.sorters.comparators.RecaptureMoveComparator;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public abstract class AbstractMoveSorterBuilder implements MoveSorterBuilder {

    protected ListenerMediator listenerMediator;

    @Override
    public AbstractMoveSorterBuilder withSmartListenerMediator(ListenerMediator listenerMediator) {
        this.listenerMediator = listenerMediator;
        return this;
    }

    @Override
    public MoveSorter build() {
        buildObjects();

        setupListeners();

        link();

        return buildSorterChain();
    }

    protected abstract void buildObjects();

    protected abstract void link();

    protected abstract void setupListeners();

    protected abstract MoveSorter buildSorterChain();


    protected MoveSorter linkMoveSorterChain(List<MoveSorter> chain) {
        for (int i = 0; i < chain.size() - 1; i++) {
            MoveSorter currentSorter = chain.get(i);
            MoveSorter next = chain.get(i + 1);
            switch (currentSorter) {
                case MoveSorterDebug moveSorterDebug -> moveSorterDebug.setNext(next);

                case RootMoveSorter rootMoveSorter -> {}

                case null -> throw new RuntimeException(String.format("MoveSorter %d is null", i));

                default ->
                        throw new RuntimeException("MoveSorter not found: " + currentSorter.getClass().getSimpleName());
            }
        }
        return chain.getFirst();
    }

    protected MoveComparator linkMoveComparatorChain(List<MoveComparator> chain) {
        for (int i = 0; i < chain.size() - 1; i++) {
            MoveComparator currentComparator = chain.get(i);
            MoveComparator next = chain.get(i + 1);
            switch (currentComparator) {
                case RecaptureMoveComparator recaptureMoveComparatorFilter ->
                        recaptureMoveComparatorFilter.setNext(next);
                case EvaluatorCacheComparator evaluatorCacheComparatorFilter ->
                        evaluatorCacheComparatorFilter.setNext(next);
                case MvvLvaComparator lvaComparator -> lvaComparator.setNext(next);
                case PromotionComparator comparator -> comparator.setNext(next);

                case null -> throw new RuntimeException(String.format("MoveComparator %d is null", i));

                default ->
                        throw new RuntimeException("MoveComparator not found: " + currentComparator.getClass().getSimpleName());
            }
        }
        return chain.getFirst();
    }
}
