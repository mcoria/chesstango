package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;

/**
 * @author Mauricio Coria
 */
public class SetKillerMoveTables implements SearchByCycleListener {
    private Move[] killerMovesTableA;
    private Move[] killerMovesTableB;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.killerMovesTableA = new Move[50];
        this.killerMovesTableB = new Move[50];

        context.setKillerMovesTableA(killerMovesTableA);
        context.setKillerMovesTableB(killerMovesTableB);
    }
}

