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
public record GroupOrder(Predicate<Move> predicate, Comparator<Move> comparator) {

    public static GroupOrder createCatchAll() {
        return new GroupOrder(_ -> true, new DefaultMoveComparator().reversed());
    }

    public static GroupOrder createPrincipalVariation(PrincipalVariationGroup principalVariationGroup) {
        return new GroupOrder(principalVariationGroup, (a, b) -> 0);
    }

    public static GroupOrder createTranspositionHeadMoveGroup(TranspositionHeadMoveGroup transpositionHeadMoveGroup) {
        return new GroupOrder(transpositionHeadMoveGroup, (a, b) -> 0);
    }

    public static GroupOrder createTranspositionTailMoveGroup(TranspositionTailMoveGroup transpositionTailMoveGroup, TranspositionTailMoveComparator transpositionTailMoveComparator) {
        return new GroupOrder(transpositionTailMoveGroup, transpositionTailMoveComparator.thenComparing(new DefaultMoveComparator().reversed()));
    }

    public int sort(List<Move> moves, int from) {
        int boundary = from;

        for (int i = 0; i < moves.size(); i++) {
            if (predicate.test(moves.get(i))) {
                Collections.swap(moves, boundary++, i);
            }
        }

        if (from + 1 < boundary) {
            moves.subList(from, boundary).sort(comparator);
        }

        return boundary;
    }
}
