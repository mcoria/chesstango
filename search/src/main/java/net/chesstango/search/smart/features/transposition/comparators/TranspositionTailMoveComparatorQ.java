package net.chesstango.search.smart.features.transposition.comparators;

import net.chesstango.search.Visitor;

/**
 * @author Mauricio Coria
 */
public class TranspositionTailMoveComparatorQ extends TranspositionTailMoveComparatorAbstract {
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}