package net.chesstango.search.alphabeta.transposition.listeners;

import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.ResetListener;
import net.chesstango.search.SearchListener;
import net.chesstango.search.alphabeta.transposition.TTableArray;

/**
 * @author Mauricio Coria
 */
@Setter
public class TTListener implements Acceptor, SearchListener, ResetListener {
    private TTableArray tTable;

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
