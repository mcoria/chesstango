package net.chesstango.search.smart.alphabeta.transposition.listeners;

import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.SearchResult;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.ResetListener;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.alphabeta.transposition.TTable;

/**
 * @author Mauricio Coria
 */
@Setter
public class ResetTranspositionTables implements SearchByCycleListener, ResetListener, Acceptor {
    protected TTable maxMap;
    protected TTable minMap;
    protected TTable qMaxMap;
    protected TTable qMinMap;

    protected boolean reuseTranspositionTable;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
    }

    @Override
    public void afterSearch(SearchResult result) {
        if (!reuseTranspositionTable) {
            reset();
        }
    }

    @Override
    public void reset() {
        this.maxMap.clear();
        this.minMap.clear();
        this.qMaxMap.clear();
        this.qMinMap.clear();
    }
}
