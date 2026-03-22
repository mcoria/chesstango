package net.chesstango.search.smart.alphabeta.transposition.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.filters.*;

/**
 *
 * @author Mauricio Coria
 */
public class LinkTTableNodeVisitor implements Visitor {
    private final TTable maxMap;
    private final TTable minMap;

    public LinkTTableNodeVisitor(TTable maxMap, TTable minMap) {
        this.maxMap = maxMap;
        this.minMap = minMap;
    }

    @Override
    public void visit(TranspositionTableRoot transpositionTableRoot) {
        transpositionTableRoot.setMaxMap(maxMap);
        transpositionTableRoot.setMinMap(minMap);
    }


    @Override
    public void visit(TranspositionTableTerminal transpositionTableTerminal) {
        transpositionTableTerminal.setMaxMap(maxMap);
        transpositionTableTerminal.setMinMap(minMap);
    }

    @Override
    public void visit(TranspositionTableLeaf transpositionTableLeaf) {
        transpositionTableLeaf.setMaxMap(maxMap);
        transpositionTableLeaf.setMinMap(minMap);
    }

    @Override
    public void visit(TranspositionTable transpositionTable) {
        transpositionTable.setMaxMap(maxMap);
        transpositionTable.setMinMap(minMap);
    }

    @Override
    public void visit(TranspositionTableQ transpositionTableQ) {
        transpositionTableQ.setMaxMap(maxMap);
        transpositionTableQ.setMinMap(minMap);
    }
}
