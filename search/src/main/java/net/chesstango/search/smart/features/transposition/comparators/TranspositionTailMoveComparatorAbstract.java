package net.chesstango.search.smart.features.transposition.comparators;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.smart.features.transposition.TTable;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;
import net.chesstango.search.smart.sorters.MoveComparator;

/**
 * @author Mauricio Coria
 */
public abstract class TranspositionTailMoveComparatorAbstract implements MoveComparator {

    @Getter
    @Setter
    private MoveComparator next;

    @Setter
    private Game game;

    @Setter
    @Getter
    private TTable maxMap;

    @Setter
    @Getter
    private TTable minMap;


    private MoveToHashMap moveToZobrist;
    private Color currentTurn;
    private TTable currentMap;

    private final TranspositionEntry moveEntry1;
    private final TranspositionEntry moveEntry2;

    public TranspositionTailMoveComparatorAbstract() {
        moveEntry1 = new TranspositionEntry();
        moveEntry2 = new TranspositionEntry();
    }

    @Override
    public void beforeSort(final int currentPly, MoveToHashMap moveToZobrist) {
        this.moveToZobrist = moveToZobrist;
        this.currentTurn = game.getPosition().getCurrentTurn();
        this.currentMap = Color.WHITE.equals(currentTurn) ? minMap : maxMap;

        next.beforeSort(currentPly, moveToZobrist);
    }

    @Override
    public void afterSort(int currentPly, MoveToHashMap moveToZobrist) {
        next.afterSort(currentPly, moveToZobrist);
    }

    @Override
    public int compare(Move o1, Move o2) {
        int result = 0;

        boolean load01 = currentMap.load(getZobristHashMove(o1), moveEntry1);
        boolean load02 = currentMap.load(getZobristHashMove(o2), moveEntry2);

        if (load01 && load02) {
            result = Color.WHITE.equals(currentTurn) ? moveEntry1.compareTo(moveEntry2) : -moveEntry1.compareTo(moveEntry2);
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