package net.chesstango.search.sorters.groupsorters;

import net.chesstango.board.moves.Move;
import net.chesstango.search.sorters.GroupSorter;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class CatchAllNullGroup implements GroupSorter {

    @Override
    public boolean offer(Move move) {
        return true;
    }

    @Override
    public void collect(List<Move> moves) {
    }

}
