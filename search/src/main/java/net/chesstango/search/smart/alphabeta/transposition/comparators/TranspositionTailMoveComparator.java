package net.chesstango.search.smart.alphabeta.transposition.comparators;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionEntry;
import net.chesstango.search.smart.sorters.MoveComparator;

/**
 * @author Mauricio Coria
 */
public class TranspositionTailMoveComparator implements MoveComparator, Acceptor {

    @Getter
    @Setter
    private MoveComparator next;

    @Setter
    @Getter
    private TTable tTable;

    @Setter
    private MoveToHashMap moveToZobrist;

    private TTable currentMap;

    private final TranspositionEntry moveEntry1;
    private final TranspositionEntry moveEntry2;

    public TranspositionTailMoveComparator() {
        moveEntry1 = new TranspositionEntry();
        moveEntry2 = new TranspositionEntry();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSort(final int currentPly) {
        this.currentMap = tTable;

        next.beforeSort(currentPly);
    }

    @Override
    public void afterSort() {
        next.afterSort();
    }

    @Override
    public int compare(Move o1, Move o2) {
        int result = 0;

        long o1Hash = getZobristHashMove(o1);
        long o2Hash = getZobristHashMove(o2);

        boolean load01 = currentMap.load(o1Hash, moveEntry1) && o1Hash == moveEntry1.getHash();
        boolean load02 = currentMap.load(o2Hash, moveEntry2) && o2Hash == moveEntry2.getHash();

        if (load01 && load02) {
            result = -moveEntry1.compareTo(moveEntry2);
        } else if (load01) {
            return 1;
        } else if (load02) {
            return -1;
        }

        return result == 0 ? next.compare(o1, o2) : result;
    }


    private long getZobristHashMove(Move move) {
        long hash = moveToZobrist.read(move);
        if (hash == 0) {
            hash = move.getZobristHash();
            moveToZobrist.write(move, hash);
        }
        return hash;
    }

}