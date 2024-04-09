package net.chesstango.search.smart.features.killermoves.comparators;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.smart.features.killermoves.KillerMoves;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.sorters.MoveComparator;

/**
 * @author Mauricio Coria
 */
public class KillerMoveComparator implements MoveComparator, SearchByCycleListener {

    @Getter
    @Setter
    private MoveComparator next;

    private int currentPly;
    private KillerMoves killerMoves;


    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.killerMoves = context.getKillerMoves();
    }

    @Override
    public void beforeSort(int currentPly, MoveToHashMap moveToZobrist) {
        this.currentPly = currentPly;
        this.next.beforeSort(currentPly, moveToZobrist);
    }

    @Override
    public void afterSort(MoveToHashMap moveToZobrist) {
        this.next.afterSort(moveToZobrist);
    }

    @Override
    public int compare(Move o1, Move o2) {
        boolean o1IsKiller = killerMoves.isKiller(o1, currentPly);

        boolean o2IsKiller = killerMoves.isKiller(o2, currentPly);

        if (o1IsKiller && !o2IsKiller) {
            return 1;
        } else if (!o1IsKiller && o2IsKiller) {
            return -1;
        }

        return next.compare(o1, o2);
    }
}
