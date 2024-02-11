package net.chesstango.search.smart.sorters.comparators;

import net.chesstango.board.moves.Move;

import java.util.Comparator;

/**
 * @author Mauricio Coria
 */
public interface MoveComparator extends Comparator<Move> {

    void beforeSort();

    void afterSort();

    @Override
    default MoveComparator reversed() {
        return new MoveComparator() {
            @Override
            public void beforeSort() {
                MoveComparator.this.beforeSort();
            }

            @Override
            public void afterSort() {
                MoveComparator.this.afterSort();
            }

            @Override
            public int compare(Move o1, Move o2) {
                return MoveComparator.this.compare(o2, o1);
            }
        };
    }
}
