package net.chesstango.search.smart.features.killermoves.listeners;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.features.killermoves.KillerMovesDebug;
import net.chesstango.search.smart.features.killermoves.KillerMovesTable;
import net.chesstango.search.visitors.SetKillerMovesVisitor;

/**
 * @author Mauricio Coria
 */
@Getter
public class SetKillerMoveTablesDebug implements SearchByCycleListener, Acceptor {

    private final KillerMovesDebug killerMovesDebug;

    @Setter
    private SearchListenerMediator searchListenerMediator;

    public SetKillerMoveTablesDebug() {
        KillerMovesTable killerMovesTable = new KillerMovesTable();

        killerMovesDebug = new KillerMovesDebug();
        killerMovesDebug.setKillerMovesImp(killerMovesTable);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        searchListenerMediator.accept(new SetKillerMovesVisitor(killerMovesDebug));
    }
}
