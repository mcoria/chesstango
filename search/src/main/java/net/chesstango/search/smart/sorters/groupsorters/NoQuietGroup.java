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
public class NoQuietGroup implements GroupSorter {

    @Setter
    @Getter
    private GroupSorter next;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSort(int currentPly) {
        next.beforeSort(currentPly);
    }

    @Override
    public void afterSort() {
        next.afterSort();
    }

    @Override
    public boolean offer(Move move) {
        if (!move.isQuiet()) {
            return next.offer(move);
        }
        return false;
    }

    @Override
    public void collect(List<Move> moves) {
        next.collect(moves);
    }
}
