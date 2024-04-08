package net.chesstango.search.smart.alphabeta.debug;

import lombok.Getter;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.killermoves.KillerMovesTable;

/**
 * @author Mauricio Coria
 */
public class SetDebugKillerMoveTables implements SearchByCycleListener {

    @Getter
    private TrapKillerMoves trapKillerMoves;

    public SetDebugKillerMoveTables() {
        KillerMovesTable killerMovesTable = new KillerMovesTable();

        trapKillerMoves = new TrapKillerMoves();
        trapKillerMoves.setKillerMovesImp(killerMovesTable);
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        context.setKillerMoves(trapKillerMoves);
    }
}
