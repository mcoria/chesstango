package net.chesstango.search.smart.sorters.groupsorters;

import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.sorters.GroupSorter;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class NoQuietGroup implements GroupSorter {

    @Override
    public boolean offer(Move move) {
        return false;
    }

    @Override
    public void collect(List<Move> moves) {

    }
}
