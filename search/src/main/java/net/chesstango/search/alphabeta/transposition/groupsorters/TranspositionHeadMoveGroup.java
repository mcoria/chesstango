package net.chesstango.search.alphabeta.transposition.groupsorters;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.transposition.TTable;
import net.chesstango.search.alphabeta.transposition.TranspositionEntry;
import net.chesstango.search.sorters.GroupSorter;
import net.chesstango.search.sorters.SortListener;

import java.util.Collections;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class TranspositionHeadMoveGroup implements Acceptor, GroupSorter, SortListener {

    private final TranspositionEntry entryWorkspace;

    @Getter
    @Setter
    private TTable tTable;

    @Setter
    private Game game;

    @Setter(AccessLevel.PACKAGE)
    @Getter(AccessLevel.PACKAGE)
    private short bestMoveEncoded;

    public TranspositionHeadMoveGroup() {
        entryWorkspace = new TranspositionEntry();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSort(int currentPly) {
        long hash = game.getPosition().getZobristHash();

        if (tTable.load(hash, entryWorkspace) && hash == entryWorkspace.getHash()) {
            bestMoveEncoded = entryWorkspace.getMove();
        } else {
            bestMoveEncoded = 0;
        }
    }

    @Override
    public int sort(List<Move> moves, int from, int to) {
        if (bestMoveEncoded != 0) {
            int pvMoveIdx = -1;

            // Find the index of the current PV move in the list of moves
            for (int i = from; i < to; i++) {
                if (moves.get(i).binaryEncoding() == bestMoveEncoded) {
                    pvMoveIdx = i;
                    break;
                }
            }

            // if current move isn't found in the list of moves
            if (pvMoveIdx == -1) {
                return from;
            }

            // Swap the current PV move with the first move in the list
            if (pvMoveIdx > from) {
                Collections.swap(moves, from, pvMoveIdx);
            }

            return from + 1;
        }

        return from;
    }
}
