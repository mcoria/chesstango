package net.chesstango.search.smart.alphabeta.transposition.listeners;

import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.ResetListener;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.alphabeta.transposition.TTableArrayPrimitives;

/**
 * @author Mauricio Coria
 */
@Setter
public class TTListener implements Acceptor, SearchByCycleListener, ResetListener {
    private TTableArrayPrimitives tTable;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        tTable.increaseAge();
    }

    @Override
    public void reset() {
        tTable.clear();
    }
}
