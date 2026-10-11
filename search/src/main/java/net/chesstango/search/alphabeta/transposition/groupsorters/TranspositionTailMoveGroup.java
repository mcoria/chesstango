package net.chesstango.search.alphabeta.transposition.groupsorters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.transposition.TTable;
import net.chesstango.search.alphabeta.transposition.TranspositionEntry;
import net.chesstango.search.sorters.GroupSorter;
import net.chesstango.search.sorters.comparators.DefaultMoveComparator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class TranspositionTailMoveGroup implements Acceptor, GroupSorter {

    private final TranspositionEntry entryWorkspace;

    @Getter
    @Setter
    private TTable tTable;

    @Setter
    private MoveToHashMap moveToZobrist;

    @Setter
    private Comparator<Move> moveComparator;


    public TranspositionTailMoveGroup() {
        entryWorkspace = new TranspositionEntry();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int sort(List<Move> moves, int from, int to) {
        int nextIdx = from;
        int lastIdx = to - 1;

        while (nextIdx < lastIdx) {

            /**
             * Find HEAD move NOT in the transposition table
             */
            for (; nextIdx < lastIdx; nextIdx++) {
                long nextHash = getZobristHashMove(moves.get(nextIdx));
                if (!tTable.load(nextHash, entryWorkspace) || nextHash != entryWorkspace.getHash()) {
                    break;
                }
            }

            /**
             * Find TAIL move in the transposition table
             */
            for (; nextIdx < lastIdx; lastIdx--) {
                long lastHash = getZobristHashMove(moves.get(lastIdx));
                if (tTable.load(lastHash, entryWorkspace) && lastHash == entryWorkspace.getHash()) {
                    break;
                }
            }

            /**
             * Swap items
             */
            if (nextIdx < lastIdx) {
                Collections.swap(moves, nextIdx, lastIdx);
                nextIdx++;
                lastIdx--;
            }
        }

        /**
         * Sort items
         */
        if (from < nextIdx) {
            moves.subList(from, nextIdx).sort(moveComparator);
        }

        return nextIdx;
    }


    long getZobristHashMove(Move move) {
        long hash = moveToZobrist.read(move);
        if (hash == 0) {
            hash = move.getZobristHash();
            moveToZobrist.write(move, hash);
        }
        return hash;
    }
}
