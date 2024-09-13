package net.chesstango.search.smart.features.killermoves.listeners;

import net.chesstango.search.SearchResult;
import net.chesstango.search.smart.features.killermoves.KillerMoves;
import net.chesstango.search.smart.features.killermoves.KillerMovesTable;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;

/**
 * @author Mauricio Coria
 */
public class SetKillerMoveTables implements SearchByCycleListener {

    private KillerMoves killerMoves;

    public SetKillerMoveTables(){
        killerMoves = new KillerMovesTable();
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        context.setKillerMoves(new KillerMovesTable());
    }

    @Override
    public void afterSearch(SearchResult result) {
        killerMoves.reset();
    }
}

