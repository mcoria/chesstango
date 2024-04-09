package net.chesstango.search.smart.features.killermoves;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.features.debug.SearchTracker;
import net.chesstango.search.smart.features.debug.model.DebugNode;

/**
 * @author Mauricio Coria
 */
public class KillerMovesDebug implements KillerMoves, SearchByCycleListener {

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
    public boolean trackKillerMove(Move move, int currentPly) {
        if(killerMovesImp.trackKillerMove(move, currentPly)){
            DebugNode currentNode = searchTracker.getCurrentNode();
            currentNode.setKillerMove(move);
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
}
