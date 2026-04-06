package net.chesstango.search.smart.sorters.groupsorters;

import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.sorters.GroupSorter;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class CollectionGroup implements GroupSorter {

    @Setter
    private GroupSorter[] groupSorters;

    @Override
    public void beforeSort(int currentPly) {
        for (GroupSorter groupSorter : groupSorters) {
            groupSorter.beforeSort(currentPly);
        }
    }

    @Override
    public void afterSort() {
        for (GroupSorter groupSorter : groupSorters) {
            groupSorter.afterSort();
        }
    }

    @Override
    public boolean offer(Move move) {
        for (GroupSorter groupSorter : groupSorters) {
            if (groupSorter.offer(move)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void collect(List<Move> moves) {
        for (GroupSorter groupSorter : groupSorters) {
            groupSorter.collect(moves);
        }
    }
}
