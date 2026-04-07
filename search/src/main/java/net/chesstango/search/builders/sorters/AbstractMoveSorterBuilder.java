package net.chesstango.search.builders.sorters;

import net.chesstango.search.smart.alphabeta.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.alphabeta.killermoves.comparators.KillerMoveComparator;
import net.chesstango.search.smart.alphabeta.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.alphabeta.pv.groupsorters.PrincipalVariationGroup;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.smart.sorters.*;
import net.chesstango.search.smart.sorters.comparators.MvvLvaComparator;
import net.chesstango.search.smart.sorters.comparators.PromotionComparator;
import net.chesstango.search.smart.sorters.comparators.RecaptureMoveComparator;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public abstract class AbstractMoveSorterBuilder {

    public MoveSorter build() {
        buildObjects();

        setupListenerMediator();

        linkObjects();

        return buildSorterChain();
    }

    protected abstract void buildObjects();

    protected abstract void setupListenerMediator();

    protected abstract void linkObjects();

    protected abstract MoveSorter buildSorterChain();


    protected MoveSorter linkMoveSorterChain(List<MoveSorter> chain) {
        for (int i = 0; i < chain.size() - 1; i++) {
            MoveSorter currentSorter = chain.get(i);
            MoveSorter next = chain.get(i + 1);
            switch (currentSorter) {
                case MoveSorterDebug moveSorterDebug -> moveSorterDebug.setNext(next);

                case RootMoveSorter rootMoveSorter -> rootMoveSorter.setNext(next);

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

                case null -> throw new RuntimeException(String.format("MoveComparator %d is null", i));

                default ->
                        throw new RuntimeException("MoveComparator not found: " + currentComparator.getClass().getSimpleName());
            }
        }
        return chain.getFirst();
    }

    protected GroupSorter linkGroupSorterChain(List<GroupSorter> chain) {
        for (int i = 0; i < chain.size() - 1; i++) {
            GroupSorter currentGroupSorter = chain.get(i);
            GroupSorter next = chain.get(i + 1);
            switch (currentGroupSorter) {

                case PrincipalVariationGroup principalVariationGroup -> principalVariationGroup.setNext(next);

                case null -> throw new RuntimeException(String.format("GroupSorter %d is null", i));

                default ->
                        throw new RuntimeException("GroupSorter not found: " + currentGroupSorter.getClass().getSimpleName());
            }
        }
        return chain.getFirst();

    }
}
