package net.chesstango.search.smart.sorters.groupsorters;

import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.sorters.GroupSorter;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class CatchAllGroup implements GroupSorter {
    private final List<Move> moves;

    private final DefaultMoveComparator defaultMoveComparator;

    public CatchAllGroup() {
        this.moves = new ArrayList<>();
        this.defaultMoveComparator = new DefaultMoveComparator();
    }

    @Override
    public boolean offer(Move move) {
        return moves.add(move);
    }

    @Override
    public void collect(List<Move> moves) {
        moves.sort(defaultMoveComparator.reversed());
    }

    @Override
    public void beforeSort(int currentPly) {
        moves.clear();
    }

    @Override
    public void afterSort() {

    }
}
