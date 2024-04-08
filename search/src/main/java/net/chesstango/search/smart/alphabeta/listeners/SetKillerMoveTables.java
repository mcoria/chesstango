package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.search.smart.killermoves.KillerMovesTable;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;

/**
 * @author Mauricio Coria
 */
public class SetKillerMoveTables implements SearchByCycleListener {

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        context.setKillerMoves(new KillerMovesTable());
    }
}

