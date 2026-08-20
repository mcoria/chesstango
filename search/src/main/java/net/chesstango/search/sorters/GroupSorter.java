package net.chesstango.search.sorters;

import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public interface GroupSorter{
    boolean offer(Move move);

    void collect(List<Move> moves);
}
