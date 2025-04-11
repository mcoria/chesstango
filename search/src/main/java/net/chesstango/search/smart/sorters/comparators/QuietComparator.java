package net.chesstango.search.smart.sorters.comparators;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.smart.sorters.MoveComparator;

/**
 * @author Mauricio Coria
 */
@Setter
@Getter
public class QuietComparator implements MoveComparator {
    private MoveComparator noQuietNext;

    private MoveComparator quietNext;

    @Override
    public void beforeSort(int currentPly, MoveToHashMap moveToZobrist) {
        noQuietNext.beforeSort(currentPly, moveToZobrist);
        quietNext.beforeSort(currentPly, moveToZobrist);
    }

    @Override
    public void afterSort(int currentPly, MoveToHashMap moveToZobrist) {
        noQuietNext.afterSort(currentPly, moveToZobrist);
        quietNext.afterSort(currentPly, moveToZobrist);
    }

    @Override
    public int compare(Move o1, Move o2) {
        if (!o1.isQuiet() && o2.isQuiet()) {
            return 1;
        } else if (o1.isQuiet() && !o2.isQuiet()) {
            return -1;
        } else if (!o1.isQuiet() && !o2.isQuiet()) {
            return noQuietNext.compare(o1, o2);
        } else if (o1.isQuiet() && o2.isQuiet()) {
            return quietNext.compare(o1, o2);
        }

        throw new RuntimeException("Imposible comparation");
    }
}
