package net.chesstango.search.smart.sorters;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;

import java.util.Comparator;

/**
 * @author Mauricio Coria
 */
public interface MoveComparator extends Comparator<Move> {

    void beforeSort(int currentPly, MoveToHashMap moveToZobrist);

    void afterSort(int currentPly, MoveToHashMap moveToZobrist);

}
