package net.chesstango.search.smart.alphabeta.transposition.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.pv.model.PVWalkerFromTT;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTable;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTableQ;

/**
 *
 * @author Mauricio Coria
 */
public class LinkTranspositionTablePVUpdate implements Visitor {
    private final PVWalkerFromTT pvWalkerFromTT;

    public LinkTranspositionTablePVUpdate(PVWalkerFromTT pvWalkerFromTT) {
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
