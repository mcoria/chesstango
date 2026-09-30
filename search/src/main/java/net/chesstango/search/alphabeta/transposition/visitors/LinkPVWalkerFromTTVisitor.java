package net.chesstango.search.alphabeta.transposition.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.pv.model.PVWalkerFromTT;
import net.chesstango.search.alphabeta.transposition.filters.TranspositionTableInterior;
import net.chesstango.search.alphabeta.transposition.filters.TranspositionTableQ;

/**
 *
 * @author Mauricio Coria
 */
public class LinkPVWalkerFromTTVisitor implements Visitor {
    private final PVWalkerFromTT pvWalkerFromTT;

    public LinkPVWalkerFromTTVisitor(PVWalkerFromTT pvWalkerFromTT) {
        this.pvWalkerFromTT = pvWalkerFromTT;
    }

    @Override
    public void visit(TranspositionTableInterior transpositionTableInterior) {
        transpositionTableInterior.setPvWalkerFromTT(pvWalkerFromTT);
    }

    @Override
    public void visit(TranspositionTableQ transpositionTableQ) {
        transpositionTableQ.setPvWalkerFromTT(pvWalkerFromTT);
    }
}
