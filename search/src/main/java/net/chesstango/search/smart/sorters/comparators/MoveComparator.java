package net.chesstango.search.smart.sorters.comparators;

import net.chesstango.board.moves.Move;

import java.util.Comparator;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public interface MoveComparator extends Comparator<Move> {

    void beforeSort(final int currentPly, Map<Short, Long> moveToZobrist);

    void afterSort(Map<Short, Long> moveToZobrist);

}
