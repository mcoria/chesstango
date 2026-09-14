package net.chesstango.search.alphabeta.killermoves.listeners;

import lombok.Setter;
import net.chesstango.search.SearchListener;
import net.chesstango.search.alphabeta.killermoves.KillerMovesTable;

/**
 * @author Mauricio Coria
 */
public class KillerMoveListener implements SearchListener {

    @Setter
    private KillerMovesTable killerMoves;

    @Override
    public void beforeSearch() {
        killerMoves.reset();
    }
}

