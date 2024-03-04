package net.chesstango.search.smart.sorters.comparators;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;

import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class KillerMoveComparator implements MoveComparator, SearchByCycleListener {

    @Getter
    @Setter
    private MoveComparator next;

    private Move[] killerMovesTableA;
    private Move[] killerMovesTableB;
    private Move killerMoveA;
    private Move killerMoveB;


    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.killerMovesTableA = context.getKillerMovesTableA();
        this.killerMovesTableB = context.getKillerMovesTableB();
    }

    @Override
    public void beforeSort(int currentPly, MoveToHashMap moveToZobrist) {
        this.killerMoveA = killerMovesTableA[currentPly - 1];
        this.killerMoveB = killerMovesTableB[currentPly - 1];
        this.next.beforeSort(currentPly, moveToZobrist);
    }

    @Override
    public void afterSort(MoveToHashMap moveToZobrist) {
        this.next.afterSort(moveToZobrist);
    }

    @Override
    public int compare(Move o1, Move o2) {
        boolean o1IsKiller = Objects.equals(o1, killerMoveA) || Objects.equals(o1, killerMoveB);

        boolean o2IsKiller = Objects.equals(o2, killerMoveA) || Objects.equals(o2, killerMoveB);

        if (o1IsKiller && !o2IsKiller) {
            return 1;
        } else if (!o1IsKiller && o2IsKiller) {
            return -1;
        }

        return next.compare(o1, o2);
    }
}
