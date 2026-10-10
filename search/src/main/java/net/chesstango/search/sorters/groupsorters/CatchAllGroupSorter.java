package net.chesstango.search.sorters.groupsorters;

import net.chesstango.board.moves.Move;
import net.chesstango.search.sorters.GroupSorter;
import net.chesstango.search.sorters.comparators.DefaultMoveComparator;

import java.util.Comparator;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class CatchAllGroupSorter implements GroupSorter {

    private final Comparator<Move> moveComparator = new DefaultMoveComparator().reversed();

    @Override
    public int sort(List<Move> moves, int from, int to) {

        moves.subList(from, to).sort(moveComparator);

        return to;
    }
}
