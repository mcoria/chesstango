package net.chesstango.search.smart.pv.groupsorters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.PVMove;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.sorters.GroupSorter;
import net.chesstango.search.sorters.SortListener;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PrincipalVariationGroup implements Acceptor, GroupSorter, SearchByCycleListener, SearchByDepthListener, SortListener {

    private final PVMove[] pvMoves;

    @Setter
    @Getter
    private GroupSorter next;

    @Setter
    private List<PVMove> lastPVMoves;

    @Setter
    private Game game;

    private int currentPly;

    public PrincipalVariationGroup() {
        pvMoves = new PVMove[40];
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        lastPVMoves = null;
        for (int i = 0; i < 40; i++) {
            pvMoves[i] = null;
        }
    }

    @Override
    public void beforeSearchByDepth() {
        if (lastPVMoves != null) {
            int i = 0;
            for (PVMove pv : lastPVMoves) {
                pvMoves[i] = pv;
                i++;
            }
        }
    }

    @Override
    public void beforeSort(int currentPly) {
        this.currentPly = currentPly;
        if (lastPVMoves != null && currentPly < lastPVMoves.size() && pvMoves[currentPly] != null) {
            long hash = game.getPosition().getZobristHash();
            PVMove pvMove = lastPVMoves.get(currentPly);
            if (pvMove.hash() != hash) {
                throw new RuntimeException("Principal variation hash mismatch");
            }
        }
    }

    @Override
    public void afterSort() {
        if (pvMoves[currentPly] != null) {
            pvMoves[currentPly] = null;
        }
    }

    @Override
    public boolean offer(Move move) {
        boolean result = false;
        if (pvMoves[currentPly] != null) {
            Move pvMove = pvMoves[currentPly].move();
            result = move.binaryEncoding() == pvMove.binaryEncoding();
        }
        return result ? result : next.offer(move);
    }

    @Override
    public void collect(List<Move> moves) {
        if (pvMoves[currentPly] != null) {
            moves.add(pvMoves[currentPly].move());
        }
        next.collect(moves);
    }
}
