package net.chesstango.search.smart.pv.comparators;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.PVMove;
import net.chesstango.search.Visitor;
import net.chesstango.search.SearchListener;
import net.chesstango.search.sorters.MoveComparator;
import net.chesstango.search.sorters.SortListener;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PrincipalVariationComparator implements MoveComparator, Acceptor, SearchListener, SortListener {

    @Getter
    @Setter
    private MoveComparator next;

    @Setter
    private List<PVMove> lastPVMoves;

    @Setter
    private Game game;

    private Move pvMove;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        lastPVMoves = null;
    }

    @Override
    public void beforeSort(int currentPly) {
        if (lastPVMoves != null) {
            if (lastPVMoves.size() > currentPly) {
                long hash = game.getPosition().getZobristHash();
                PVMove pvMove = lastPVMoves.get(currentPly);
                if (pvMove.hash() == hash) {
                    this.pvMove = pvMove.move();
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
