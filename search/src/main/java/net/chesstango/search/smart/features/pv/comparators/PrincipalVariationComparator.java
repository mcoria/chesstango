package net.chesstango.search.smart.features.pv.comparators;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.sorters.MoveComparator;

import java.util.List;
import java.util.Queue;

/**
 * @author Mauricio Coria
 */
public class PrincipalVariationComparator implements MoveComparator, SearchByDepthListener {


    @Getter
    @Setter
    private MoveComparator next;
    private List<PrincipalVariation> lastPrincipalVariation;

    private Move pvMove;
    private boolean pvConsumed;

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        this.lastPrincipalVariation = context.getLastPrincipalVariation();
    }

    @Override
    public void beforeSort(int currentPly, MoveToHashMap moveToZobrist) {
        this.next.beforeSort(currentPly, moveToZobrist);
        if (lastPrincipalVariation != null && !lastPrincipalVariation.isEmpty()) {
            //this.pvMove = lastPrincipalVariation.poll();
            //this.pvConsumed = false;
        }
    }

    @Override
    public void afterSort(int currentPly, MoveToHashMap moveToZobrist) {
        this.next.afterSort(currentPly, moveToZobrist);
        /*
        if (pvMove != null && !pvConsumed) {
            throw new RuntimeException("PV move not consumed");
        }

         */
        this.pvMove = null;
    }

    @Override
    public int compare(Move o1, Move o2) {
        /*
        if (pvMove != null) {
            if (pvMove.equals(o1)) {
                pvConsumed = true;
                return 1;
            } else if (pvMove.equals(o2)) {
                pvConsumed = true;
                return -1;
            }
        }
         */
        return next.compare(o1, o2);
    }
}
