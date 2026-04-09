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
public class PVCalculatorDebug implements PVCalculator, Acceptor {

    @Setter
    @Getter
    private PVCalculator imp;

    @Setter
    private SearchTracker searchTracker;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void calculatePrincipalVariation(short secondMovePV, int bestValue) {
        DebugNode currentNode = searchTracker.getCurrentNode(); //El root node

        currentNode.readingPrincipalVariationON();

        imp.calculatePrincipalVariation(secondMovePV, bestValue);

        currentNode.readingPrincipalVariationOFF();
    }

}
