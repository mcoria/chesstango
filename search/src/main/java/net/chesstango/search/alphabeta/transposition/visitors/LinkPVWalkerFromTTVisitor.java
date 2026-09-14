package net.chesstango.search.alphabeta.transposition.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.pv.model.PVWalkerFromTT;
import net.chesstango.search.alphabeta.transposition.filters.TranspositionTable;
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
    public void visit(TranspositionTable transpositionTable) {
        transpositionTable.setPvWalkerFromTT(pvWalkerFromTT);
    }

    @Override
    public void visit(TranspositionTableQ transpositionTableQ) {
        transpositionTableQ.setPvWalkerFromTT(pvWalkerFromTT);
    }
}
