package net.chesstango.search.smart.sorters.comparators;

import net.chesstango.board.moves.Move;

import java.util.Comparator;

/**
 * @author Mauricio Coria
 */
public interface MoveComparator extends Comparator<Move> {


    @Override
    default MoveComparator reversed() {
        return (m1, m2) -> this.compare(m2, m1);
    }
}
