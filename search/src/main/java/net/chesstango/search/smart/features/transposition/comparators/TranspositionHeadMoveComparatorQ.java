package net.chesstango.search.smart.features.transposition.comparators;

import net.chesstango.search.Visitor;

/**
 * @author Mauricio Coria
 */
public class TranspositionHeadMoveComparatorQ extends TranspositionHeadMoveComparatorAbstract {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
