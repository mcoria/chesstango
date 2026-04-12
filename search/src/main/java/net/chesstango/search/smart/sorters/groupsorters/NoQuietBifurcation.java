package net.chesstango.search.smart.sorters.groupsorters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.sorters.GroupSorter;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class NoQuietBifurcation implements GroupSorter {

    private final boolean noQuietFirst;

    @Setter
    @Getter
    private GroupSorter quietGroup;

    @Setter
    @Getter
    private GroupSorter noQuietGroup;

    public NoQuietBifurcation(boolean noQuietFirst) {
        this.noQuietFirst = noQuietFirst;
    }

    @Override
    public void beforeSort(int currentPly) {
        quietGroup.beforeSort(currentPly);
        noQuietGroup.beforeSort(currentPly);
    }

    @Override
    public void afterSort() {
        quietGroup.afterSort();
        noQuietGroup.afterSort();
    }

    @Override
    public boolean offer(Move move) {
        if (move.isQuiet()) {
            return quietGroup.offer(move);
        }
        return noQuietGroup.offer(move);
    }

    @Override
    public void collect(List<Move> moves) {
        if (noQuietFirst) {
            noQuietGroup.collect(moves);
            quietGroup.collect(moves);
        } else {
            quietGroup.collect(moves);
            noQuietGroup.collect(moves);
        }
    }
}
