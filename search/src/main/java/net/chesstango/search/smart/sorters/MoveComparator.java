package net.chesstango.search.smart.sorters;

import net.chesstango.board.moves.Move;

import java.util.Comparator;

/**
 * @author Mauricio Coria
 */
public interface MoveComparator extends Comparator<Move>, SortListener {
}
