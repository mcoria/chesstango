package net.chesstango.search.smart.alphabeta.debug;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.killermoves.KillerMoves;

/**
 * @author Mauricio Coria
 */
public class TrapKillerMoves implements KillerMoves, SearchByCycleListener {

    @Getter
    @Setter
    private SearchTracker searchTracker;

    @Setter
    @Getter
    private KillerMoves killerMovesImp;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.searchTracker = context.getSearchTracker();
    }

    @Override
    public boolean trackKillerMove(Move killerMove, int currentPly) {
        if(killerMovesImp.trackKillerMove(killerMove, currentPly)){
            DebugNode currentNode = searchTracker.getCurrentNode();
            currentNode.setKillerMove(killerMove);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isKiller(Move move, int currentPly) {
        return killerMovesImp.isKiller(move, currentPly);
    }

    @Override
    public void reset() {
        killerMovesImp.reset();;
    }

    /**
     * Este metodo deberia moverse una vez tengamos el wrapper de killer move tables
     */
    private void trackKillerMoves(int currentPly) {
        /*
        DebugNode currentNode = searchTracker.getCurrentNode();
        if (currentPly > 0) {
            if (killerMovesTableA != null || killerMovesTableB != null) {
                if (killerMovesTableA[currentPly - 1] != null) {
                    currentNode.setSorterKmA(killerMovesTableA[currentPly - 1]);
                }
                if (killerMovesTableB[currentPly - 1] != null) {
                    currentNode.setSorterKmB(killerMovesTableB[currentPly - 1]);
                }
            }
        }

         */
    }
}
