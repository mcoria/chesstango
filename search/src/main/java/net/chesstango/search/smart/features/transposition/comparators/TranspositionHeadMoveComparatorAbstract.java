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
@Setter
public abstract class TranspositionHeadMoveComparatorAbstract implements MoveComparator {

    @Getter
    private MoveComparator next;

    private Game game;

    private TTable maxMap;
    private TTable minMap;

    private short bestMoveEncoded;

    private final TranspositionEntry entryWorkspace;

    public TranspositionHeadMoveComparatorAbstract() {
        entryWorkspace = new TranspositionEntry();
    }

    @Override
    public void beforeSort(final int currentPly, MoveToHashMap moveToZobrist) {
        final Color currentTurn = game.getPosition().getCurrentTurn();

        long hash = game.getPosition().getZobristHash();

        boolean load = Color.WHITE.equals(currentTurn) ?
                maxMap.load(hash, entryWorkspace) : minMap.load(hash, entryWorkspace);

        if (load) {
            bestMoveEncoded = entryWorkspace.getMove();
        } else {
            bestMoveEncoded = 0;
        }

        next.beforeSort(currentPly, moveToZobrist);
    }

    @Override
    public void afterSort(int currentPly, MoveToHashMap moveToZobrist) {
        next.afterSort(currentPly, moveToZobrist);
    }


    @Override
    public int compare(Move o1, Move o2) {
        if (bestMoveEncoded != 0) {
            if (o1.binaryEncoding() == bestMoveEncoded) {
                return 1;
            } else if (o2.binaryEncoding() == bestMoveEncoded) {
                return -1;
            }
        }

        return next.compare(o1, o2);
    }
}
