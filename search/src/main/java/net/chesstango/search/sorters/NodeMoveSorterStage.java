package net.chesstango.search.sorters;

import net.chesstango.board.moves.Move;
import net.chesstango.search.alphabeta.pv.groupsorters.PrincipalVariationGroup;
import net.chesstango.search.alphabeta.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.alphabeta.transposition.groupsorters.TranspositionHeadMoveGroup;
import net.chesstango.search.alphabeta.transposition.groupsorters.TranspositionTailMoveGroup;
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

    public static NodeMoveSorterStage createPrincipalVariationStage(PrincipalVariationGroup principalVariationGroup) {
        return new NodeMoveSorterStage(principalVariationGroup, (a, b) -> 0);
    }

    public static NodeMoveSorterStage createTranspositionHeadStage(TranspositionHeadMoveGroup transpositionHeadMoveGroup) {
        return new NodeMoveSorterStage(transpositionHeadMoveGroup, (a, b) -> 0);
    }

    public static NodeMoveSorterStage createTranspositionTailStage(TranspositionTailMoveGroup transpositionTailMoveGroup, TranspositionTailMoveComparator transpositionTailMoveComparator) {
        return new NodeMoveSorterStage(transpositionTailMoveGroup, transpositionTailMoveComparator.thenComparing(new DefaultMoveComparator().reversed()));
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
