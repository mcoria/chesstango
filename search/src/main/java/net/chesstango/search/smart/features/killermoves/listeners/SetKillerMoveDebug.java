package net.chesstango.search.smart.features.killermoves.listeners;

import lombok.Getter;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.features.killermoves.KillerMovesDebug;
import net.chesstango.search.smart.features.killermoves.KillerMovesTable;

/**
 * @author Mauricio Coria
 */
public class SetKillerMoveDebug implements SearchByCycleListener {

    @Getter
    private KillerMovesDebug killerMovesDebug;

    public SetKillerMoveDebug() {
        KillerMovesTable killerMovesTable = new KillerMovesTable();

        killerMovesDebug = new KillerMovesDebug();
        killerMovesDebug.setKillerMovesImp(killerMovesTable);
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        context.setKillerMoves(killerMovesDebug);
    }
}
