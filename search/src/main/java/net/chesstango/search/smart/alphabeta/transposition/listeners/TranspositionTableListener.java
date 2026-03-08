package net.chesstango.search.smart.alphabeta.transposition.listeners;

import lombok.Setter;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.ResetListener;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.alphabeta.transposition.TTable;

/**
 * @author Mauricio Coria
 */
@Setter
public class TranspositionTableListener implements SearchByCycleListener, ResetListener {
    protected TTable maxMap;
    protected TTable minMap;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        maxMap.increaseAge();
        minMap.increaseAge();
    }

    @Override
    public void reset() {
        maxMap.clear();
        minMap.clear();
    }
}
