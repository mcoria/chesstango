package net.chesstango.search.sorters.groupsorters;

import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.sorters.GroupSorter;

import java.util.Comparator;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class CatchAllGroup implements GroupSorter {

    @Setter
    private Comparator<Move> moveComparator;

    @Override
    public int sort(List<Move> moves, int from, int to) {

        moves.subList(from, to).sort(moveComparator);

        return to;
    }
}
