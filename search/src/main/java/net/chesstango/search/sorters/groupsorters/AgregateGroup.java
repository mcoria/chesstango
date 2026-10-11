package net.chesstango.search.sorters.groupsorters;

import net.chesstango.board.moves.Move;
import net.chesstango.search.sorters.GroupSorter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AgregateGroup implements GroupSorter {
    private final List<GroupSorter> groupSorters = new ArrayList<>();

    @Override
    public int sort(List<Move> moves, int from, int to) {

        int currentIdx = from;

        for (GroupSorter groupSorter : groupSorters) {
            currentIdx = groupSorter.sort(moves, currentIdx, to);
        }

        return currentIdx;
    }

    public void addGroup(GroupSorter groupSorter) {
        groupSorters.add(groupSorter);
    }
}
