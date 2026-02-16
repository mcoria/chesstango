package net.chesstango.search.smart.features.killermoves.listeners;

import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.features.killermoves.KillerMoves;
import net.chesstango.search.smart.features.killermoves.KillerMovesTable;
import net.chesstango.search.smart.features.killermoves.visitors.SetKillerMovesVisitor;

/**
 * @author Mauricio Coria
 */
public class SetKillerMoveTables implements SearchByCycleListener, Acceptor {

    private KillerMoves killerMoves;

    @Setter
    private SearchListenerMediator searchListenerMediator;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        if(killerMoves == null) {
            killerMoves = new KillerMovesTable();
            searchListenerMediator.accept(new SetKillerMovesVisitor(killerMoves));
        } else {
            killerMoves.reset();
        }
    }
}

