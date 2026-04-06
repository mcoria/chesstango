package net.chesstango.search.smart.sorters.groupsorters;

import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.sorters.GroupSorter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class NoQuietGroup implements GroupSorter {

    private final List<Move> moves;

    public NoQuietGroup() {
        this.moves = new ArrayList<>();
    }

    @Override
    public void beforeSort(int currentPly) {
        moves.clear();
    }

    @Override
    public void afterSort() {
    }

    @Override
    public boolean offer(Move move) {
        if (!move.isQuiet()) {
            moves.add(move);
            return true;
        }
        return false;
    }

    @Override
    public void collect(List<Move> moves) {
    }
}
