package net.chesstango.search.smart.alphabeta.transposition.comparators;

import net.chesstango.search.Visitor;

/**
 * @author Mauricio Coria
 */
public class TranspositionTailMoveComparator extends TranspositionTailMoveComparatorAbstract {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
