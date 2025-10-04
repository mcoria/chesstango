package net.chesstango.search.smart.features.transposition.comparators;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.features.transposition.TTable;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;
import net.chesstango.search.smart.sorters.MoveComparator;

import java.util.function.Function;

/**
 * @author Mauricio Coria
 */
public class TranspositionTailMoveComparator implements MoveComparator, SearchByCycleListener {
    private final Function<SearchByCycleContext, TTable> fnGetMaxMap;
    private final Function<SearchByCycleContext, TTable> fnGetMinMap;

    @Getter
    @Setter
    private MoveComparator next;

    private Game game;
    private TTable maxMap;
    private TTable minMap;
    private MoveToHashMap moveToZobrist;
    private Color currentTurn;
    private TTable currentMap;

    public TranspositionTailMoveComparator(Function<SearchByCycleContext, TTable> fnGetMaxMap, Function<SearchByCycleContext, TTable> fnGetMinMap) {
        this.fnGetMaxMap = fnGetMaxMap;
        this.fnGetMinMap = fnGetMinMap;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.maxMap = fnGetMaxMap.apply(context);
        this.minMap = fnGetMinMap.apply(context);
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

        final TranspositionEntry moveEntry1 = currentMap.read(getZobristHashMove(o1));
        final TranspositionEntry moveEntry2 = currentMap.read(getZobristHashMove(o2));

        if (moveEntry1 != null && moveEntry2 != null) {
            result = Color.WHITE.equals(currentTurn) ? moveEntry1.compareTo(moveEntry2) : -moveEntry1.compareTo(moveEntry2);
        } else if (moveEntry1 != null) {
            return 1;
        } else if (moveEntry2 != null) {
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
