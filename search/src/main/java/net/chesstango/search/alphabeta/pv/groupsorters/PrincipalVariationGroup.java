package net.chesstango.search.alphabeta.pv.groupsorters;

import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.*;
import net.chesstango.search.sorters.SortListener;

import java.util.List;
import java.util.function.Predicate;

import static net.chesstango.search.alphabeta.Constants.MAX_DEPTH;

/**
 * @author Mauricio Coria
 */
public class PrincipalVariationGroup implements Acceptor, Predicate<Move>, SearchListener, SearchByDepthListener, SortListener {
    @Setter
    private List<PVMove> lastPVMoves;

    private final PVMove[] pvMoves;

    private PVMove currentPVMove;

    public PrincipalVariationGroup() {
        pvMoves = new PVMove[MAX_DEPTH];
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        lastPVMoves = null;
    }

    @Override
    public void beforeSearchByDepth() {
        for (int i = 0; i < MAX_DEPTH; i++) {
            if (lastPVMoves != null && i < lastPVMoves.size()) {
                pvMoves[i] = lastPVMoves.get(i);
            } else {
                pvMoves[i] = null;
            }
        }
    }

    @Override
    public void beforeSort(int currentPly) {
        if (pvMoves[currentPly] != null) {
            currentPVMove = pvMoves[currentPly];
            pvMoves[currentPly] = null;
        } else {
            currentPVMove = null;
        }
    }

    @Override
    public boolean test(Move move) {
        return currentPVMove != null && move.equals(currentPVMove.move());
    }

}
