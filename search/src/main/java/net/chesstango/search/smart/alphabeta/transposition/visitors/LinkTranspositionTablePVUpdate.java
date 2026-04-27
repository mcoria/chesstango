package net.chesstango.search.smart.alphabeta.transposition.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTable;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTablePVUpdate;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTableQ;

/**
 *
 * @author Mauricio Coria
 */
public class LinkTranspositionTablePVUpdate implements Visitor {
    private final TranspositionTablePVUpdate transpositionTablePVUpdate;

    public LinkTranspositionTablePVUpdate(TranspositionTablePVUpdate transpositionTablePVUpdate) {
        this.transpositionTablePVUpdate = transpositionTablePVUpdate;
    }

    @Override
    public void visit(TranspositionTable transpositionTable) {
        transpositionTable.setTranspositionTablePVUpdate(transpositionTablePVUpdate);
    }

    @Override
    public void visit(TranspositionTableQ transpositionTableQ) {
        transpositionTableQ.setTranspositionTablePVUpdate(transpositionTablePVUpdate);
    }
}
