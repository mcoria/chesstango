package net.chesstango.search.smart.pv.comparators;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.sorters.MoveComparator;
import net.chesstango.search.sorters.SortListener;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PrincipalVariationComparator implements MoveComparator, Acceptor, SearchByCycleListener, SortListener {

    @Getter
    @Setter
    private MoveComparator next;

    @Setter
    private List<PrincipalVariation> lastPrincipalVariations;

    @Setter
    private Game game;

    private Move pvMove;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        lastPrincipalVariations = null;
    }

    @Override
    public void beforeSort(int currentPly) {
        if (lastPrincipalVariations != null) {
            if (lastPrincipalVariations.size() > currentPly) {
                long hash = game.getPosition().getZobristHash();
                PrincipalVariation principalVariation = lastPrincipalVariations.get(currentPly);
                if (principalVariation.hash() == hash) {
                    pvMove = principalVariation.move();
                }
            }
        }
    }

    @Override
    public void afterSort() {
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
