package net.chesstango.search.smart.features.killermoves.listeners;

import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.SearchResult;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.features.killermoves.KillerMoves;
import net.chesstango.search.smart.features.killermoves.KillerMovesTable;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.visitors.SetKillerMovesVisitor;

/**
 * @author Mauricio Coria
 */
public class SetKillerMoveTables implements SearchByCycleListener, Acceptor {

    private final KillerMoves killerMoves;

    @Setter
    private SearchListenerMediator searchListenerMediator;

    public SetKillerMoveTables(){
        killerMoves = new KillerMovesTable();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        searchListenerMediator.accept(new SetKillerMovesVisitor(killerMoves));
    }

    @Override
    public void afterSearch(SearchResult result) {
        killerMoves.reset();
    }
}

