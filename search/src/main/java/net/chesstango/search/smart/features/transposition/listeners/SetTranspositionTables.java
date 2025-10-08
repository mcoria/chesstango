package net.chesstango.search.smart.features.transposition.listeners;

import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.SearchResult;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.ResetListener;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.features.transposition.TTableArray;
import net.chesstango.search.smart.features.transposition.TTable;
import net.chesstango.search.visitors.SetTTableVisitor;

/**
 * @author Mauricio Coria
 */
@Setter
public class SetTranspositionTables implements SearchByCycleListener, ResetListener, Acceptor {
    protected final TTable maxMap;
    protected final TTable minMap;
    protected final TTable qMaxMap;
    protected final TTable qMinMap;

    protected boolean reuseTranspositionTable;

    private SearchListenerMediator searchListenerMediator;

    public SetTranspositionTables() {
        this.maxMap = createMaxTTable();
        this.minMap = createMinTTable();
        this.qMaxMap = createQMaxTTable();
        this.qMinMap = createQMinTTable();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        searchListenerMediator.accept(new SetTTableVisitor(maxMap, minMap, qMaxMap, qMinMap));
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

    protected TTable createQMinTTable() {
        return defaultCreateTTable();
    }

    protected TTable createQMaxTTable() {
        return defaultCreateTTable();
    }

    protected TTable createMinTTable() {
        return defaultCreateTTable();
    }

    protected TTable createMaxTTable() {
        return defaultCreateTTable();
    }

    protected TTable defaultCreateTTable() {
        return new TTableArray();
    }
}
