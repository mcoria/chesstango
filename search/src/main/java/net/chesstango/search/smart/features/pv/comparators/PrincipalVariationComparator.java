package net.chesstango.search.smart.features.pv.comparators;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.sorters.MoveComparator;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PrincipalVariationComparator implements MoveComparator, SearchByCycleListener, SearchByDepthListener {


    @Getter
    @Setter
    private MoveComparator next;
    private List<PrincipalVariation> lastPrincipalVariation;

    private Game game;
    private Move pvMove;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        this.lastPrincipalVariation = context.getLastPrincipalVariation();
    }

    @Override
    public void beforeSort(int currentPly, MoveToHashMap moveToZobrist) {
        this.next.beforeSort(currentPly, moveToZobrist);
        if (lastPrincipalVariation != null) {
            if (lastPrincipalVariation.size() > currentPly) {
                long hash = game.getPosition().getZobristHash();
                PrincipalVariation principalVariation = lastPrincipalVariation.get(currentPly);
                if (principalVariation.hash() == hash) {
                    pvMove = principalVariation.move();
                }
            }
        }
    }

    @Override
    public void afterSort(int currentPly, MoveToHashMap moveToZobrist) {
        this.next.afterSort(currentPly, moveToZobrist);
        this.pvMove = null;
    }

    @Override
    public int compare(Move o1, Move o2) {
        if (pvMove != null) {
            if (pvMove.equals(o1)) {
                return 1;
            } else if (pvMove.equals(o2)) {
                return -1;
            }
        }
        return next.compare(o1, o2);
    }
}
