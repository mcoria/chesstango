package net.chesstango.search.smart.sorters.comparators;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;

/**
 * @author Mauricio Coria
 */
public class QuietComparator implements MoveComparator {
    @Getter
    @Setter
    private MoveComparator next;

    @Override
    public void beforeSort(int currentPly, MoveToHashMap moveToZobrist) {
        next.beforeSort(currentPly, moveToZobrist);
    }

    @Override
    public void afterSort(MoveToHashMap moveToZobrist) {
        next.afterSort(moveToZobrist);
    }

    @Override
    public int compare(Move o1, Move o2) {
        if (!o1.isQuiet() && o2.isQuiet()) {
            return 1;
        } else if (o1.isQuiet() && !o2.isQuiet()) {
            return -1;
        }

        return next.compare(o1, o2);
    }
}
