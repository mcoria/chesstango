package net.chesstango.search.smart.killermoves.listeners;

import lombok.Setter;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.killermoves.KillerMovesTable;

/**
 * @author Mauricio Coria
 */
public class KillerMoveListener implements SearchByCycleListener {

    @Setter
    private KillerMovesTable killerMoves;

    @Override
    public void beforeSearch() {
        killerMoves.reset();
    }
}

