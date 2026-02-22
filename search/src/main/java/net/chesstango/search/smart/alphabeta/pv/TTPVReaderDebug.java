package net.chesstango.search.smart.alphabeta.pv;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.debug.SearchTracker;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;

/**
 * @author Mauricio Coria
 */
public class TTPVReaderDebug implements PVReader, Acceptor {

    @Setter
    @Getter
    private TTPVReader imp;

    @Setter
    private SearchTracker searchTracker;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void readPrincipalVariation(short bestMove, int bestValue) {
        DebugNode currentNode = searchTracker.getCurrentNode(); //El root node

        currentNode.readingPrincipalVariationON();

        imp.readPrincipalVariation(bestMove, bestValue);

        currentNode.readingPrincipalVariationOFF();
    }

}
