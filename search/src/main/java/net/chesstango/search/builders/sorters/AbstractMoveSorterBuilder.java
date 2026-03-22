package net.chesstango.search.builders.sorters;

import net.chesstango.search.smart.alphabeta.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.alphabeta.killermoves.comparators.KillerMoveComparator;
import net.chesstango.search.smart.alphabeta.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.smart.sorters.MoveComparator;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.MoveSorterDebug;
import net.chesstango.search.smart.sorters.RootMoveSorter;
import net.chesstango.search.smart.sorters.comparators.MvvLvaComparator;
import net.chesstango.search.smart.sorters.comparators.PromotionComparator;
import net.chesstango.search.smart.sorters.comparators.RecaptureMoveComparator;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public abstract class AbstractMoveSorterBuilder {

    protected MoveSorter buildChain(List<MoveSorter> chain) {
        for (int i = 0; i < chain.size() - 1; i++) {
            MoveSorter currentSorter = chain.get(i);
            MoveSorter next = chain.get(i + 1);
            switch (currentSorter) {
                case RootMoveSorter rootMoveSorter -> rootMoveSorter.setNext(next);

                case MoveSorterDebug moveSorterDebug -> moveSorterDebug.setNext(next);

                case null -> throw new RuntimeException(String.format("sorter %d is null", i));

                default -> throw new RuntimeException("sorter not found: " + currentSorter.getClass().getSimpleName());
            }
        }
        return chain.getFirst();
    }

    protected MoveComparator linkComparatorChain(List<MoveComparator> chain) {
        for (int i = 0; i < chain.size() - 1; i++) {
            MoveComparator currentComparator = chain.get(i);
            MoveComparator next = chain.get(i + 1);
            switch (currentComparator) {
                case TranspositionHeadMoveComparator headMoveComparator -> headMoveComparator.setNext(next);
                case TranspositionTailMoveComparator tailMoveComparator -> tailMoveComparator.setNext(next);
                case RecaptureMoveComparator recaptureMoveComparatorFilter ->
                        recaptureMoveComparatorFilter.setNext(next);
                case GameEvaluatorCacheComparator gameEvaluatorCacheComparatorFilter ->
                        gameEvaluatorCacheComparatorFilter.setNext(next);
                case KillerMoveComparator moveComparator -> moveComparator.setNext(next);
                case MvvLvaComparator lvaComparator -> lvaComparator.setNext(next);
                case PromotionComparator comparator -> comparator.setNext(next);
                case PrincipalVariationComparator variationComparator -> variationComparator.setNext(next);

                case null -> throw new RuntimeException(String.format("comparator %d is null", i));

                default ->
                        throw new RuntimeException("comparator not found: " + currentComparator.getClass().getSimpleName());
            }
        }
        return chain.getFirst();
    }
}
