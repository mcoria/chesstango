package net.chesstango.search.smart.alphabeta.pv;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.debug.DebugNodeTracker;

/**
 * @author Mauricio Coria
 */
public class PVCalculatorDebug implements PVCalculator, Acceptor {

    @Setter
    @Getter
    private PVCalculator imp;

    @Setter
    private DebugNodeTracker debugNodeTracker;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void calculatePrincipalVariation(int eval) {
        //DebugNode currentNode = searchTracker.getCurrentNode(); //El root node

        //currentNode.readingPrincipalVariationON();

        imp.calculatePrincipalVariation(eval);

        //.readingPrincipalVariationOFF();
    }

}
