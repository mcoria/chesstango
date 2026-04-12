package net.chesstango.search.smart.sorters.groupsorters;

import net.chesstango.board.moves.Move;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.sorters.GroupSorter;

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

    @Override
    public void beforeSort(int currentPly) {
    }

    @Override
    public void afterSort() {
    }
}
