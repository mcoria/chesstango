package net.chesstango.search.smart.sorters.comparators;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.GameStateReader;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.sorters.MoveComparator;

import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class RecaptureMoveComparator implements MoveComparator, SearchByCycleListener {

    @Getter
    @Setter
    private MoveComparator next;
    private Game game;
    private Square previousMoveToSquare;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
    }

    @Override
    public void beforeSort(final int currentPly, MoveToHashMap moveToZobrist) {
        GameStateReader previousState = this.game.getState().getPreviousState();
        Move previousMove = previousState.getSelectedMove();
        if (previousMove != null && !previousMove.isQuiet()) {
            previousMoveToSquare = previousMove.getTo().getSquare();
        } else {
            previousMoveToSquare = null;
        }

        next.beforeSort(currentPly, moveToZobrist);
    }

    @Override
    public void afterSort(MoveToHashMap moveToZobrist) {
        next.afterSort(moveToZobrist);
    }

    @Override
    public int compare(Move o1, Move o2) {
        if (previousMoveToSquare != null) {
            Square o1ToSquare = o1.getTo().getSquare();
            Square o2ToSquare = o2.getTo().getSquare();

            if (!o1ToSquare.equals(o2ToSquare)) {
                if (Objects.equals(previousMoveToSquare, o1ToSquare)) {
                    return 1;
                } else if (Objects.equals(previousMoveToSquare, o2ToSquare)) {
                    return -1;
                }
            }
        }
        return next.compare(o1, o2);
    }

}
