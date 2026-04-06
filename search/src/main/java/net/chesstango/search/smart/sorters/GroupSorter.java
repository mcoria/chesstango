package net.chesstango.search.smart.sorters;

import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public interface GroupSorter extends Acceptor, SortListener {
    boolean offer(Move move);

    void collect(List<Move> moves);
}
