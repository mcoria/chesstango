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
    private MoveComparator noQuietNext;

    @Getter
    @Setter
    private MoveComparator quietNext;

    @Override
    public void beforeSort(int currentPly, MoveToHashMap moveToZobrist) {
        noQuietNext.beforeSort(currentPly, moveToZobrist);
        quietNext.beforeSort(currentPly, moveToZobrist);
    }

    @Override
    public void afterSort(MoveToHashMap moveToZobrist) {
        noQuietNext.afterSort(moveToZobrist);
        quietNext.afterSort(moveToZobrist);
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
