package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.transposition.MapTTable;
import net.chesstango.search.smart.transposition.QTransposition;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.Transposition;

/**
 * @author Mauricio Coria
 */
public class SetTranspositionTables implements SearchLifeCycle {
    private final TTable<Transposition> tTable;

    private final TTable<QTransposition> qTTable;

    public SetTranspositionTables(){
        this.tTable = new MapTTable<>();
        this.qTTable = new MapTTable<>();
    }

    @Override
    public void beforeSearch(Game game, int maxDepth) {
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        context.setTTable(tTable);
        context.setQTTable(qTTable);
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }

    @Override
    public void stopSearching() {
    }

    @Override
    public void reset() {
    }

}
