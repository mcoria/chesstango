package net.chesstango.search.alphabeta.pv.groupsorters;

import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.*;
import net.chesstango.search.sorters.GroupSorter;
import net.chesstango.search.sorters.SortListener;

import java.util.Collections;
import java.util.List;

import static net.chesstango.search.alphabeta.Constants.MAX_DEPTH;

/**
 * @author Mauricio Coria
 */
public class PrincipalVariationGroup implements Acceptor, GroupSorter, SearchListener, SearchByDepthListener, SortListener {

    private final PVMove[] pvMoves;

    @Setter
    private List<PVMove> lastPVMoves;

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
        }
    }

    @Override
    public int sort(List<Move> moves, int from, int to) {
        if (currentPVMove != null) {
            int pvMoveIdx = -1;

            // Find the index of the current PV move in the list of moves
            for (int i = from; i < to; i++) {
                if (moves.get(i).equals(currentPVMove.move())) {
                    pvMoveIdx = i;
                    break;
                }
            }

            // if current PV move isn't found in the list of moves
            if (pvMoveIdx == -1) {
                throw new IllegalStateException("Current PV move not found in the list of moves");
            }

            // Swap the current PV move with the first move in the list
            if (pvMoveIdx > from) {
                Collections.swap(moves, from, pvMoveIdx);
            }

            // Reset current PV move
            currentPVMove = null;

            return from + 1;
        }

        return from;
    }
}
