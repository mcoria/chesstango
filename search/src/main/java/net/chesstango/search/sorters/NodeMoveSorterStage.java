package net.chesstango.search.sorters;

import net.chesstango.board.moves.Move;
import net.chesstango.search.alphabeta.pv.sorters.PrincipalVariationPredicate;
import net.chesstango.search.alphabeta.transposition.sorters.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.alphabeta.transposition.sorters.TranspositionHeadPredicate;
import net.chesstango.search.alphabeta.transposition.sorters.TranspositionTailPredicate;
import net.chesstango.search.sorters.comparators.DefaultMoveComparator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
public record NodeMoveSorterStage(Predicate<Move> predicate, Comparator<Move> comparator) {

    public static NodeMoveSorterStage createCatchAllStage() {
        return new NodeMoveSorterStage(_ -> true, new DefaultMoveComparator().reversed());
    }

    public static NodeMoveSorterStage createPrincipalVariationStage(PrincipalVariationPredicate principalVariationPredicate) {
        return new NodeMoveSorterStage(principalVariationPredicate, (a, b) -> 0);
    }

    public static NodeMoveSorterStage createTranspositionHeadStage(TranspositionHeadPredicate transpositionHeadPredicate) {
        return new NodeMoveSorterStage(transpositionHeadPredicate, (a, b) -> 0);
    }

    public static NodeMoveSorterStage createTranspositionTailStage(TranspositionTailPredicate transpositionTailPredicate, TranspositionTailMoveComparator transpositionTailMoveComparator) {
        return new NodeMoveSorterStage(transpositionTailPredicate, transpositionTailMoveComparator.thenComparing(new DefaultMoveComparator().reversed()));
    }

    public int sort(List<Move> moves, int from) {
        int boundary = from;

        for (int i = from; i < moves.size(); i++) {
            if (predicate.test(moves.get(i))) {
                if (boundary < i) {
                    Collections.swap(moves, boundary, i);
                }
                boundary++;
            }
        }

        if (from + 1 < boundary) {
            moves.subList(from, boundary).sort(comparator);
        }

        return boundary;
    }
}
