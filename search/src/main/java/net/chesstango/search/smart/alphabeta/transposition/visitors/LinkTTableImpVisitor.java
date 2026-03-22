package net.chesstango.search.smart.alphabeta.transposition.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.pv.TTPVReader;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.smart.alphabeta.transposition.filters.*;
import net.chesstango.search.smart.alphabeta.transposition.listeners.TTDump;
import net.chesstango.search.smart.alphabeta.transposition.listeners.TTLoad;

/**
 *
 * @author Mauricio Coria
 */
public class LinkTTableImpVisitor implements Visitor {
    private final TTable maxMap;
    private final TTable minMap;

    public LinkTTableImpVisitor(TTable maxMap, TTable minMap) {
        this.maxMap = maxMap;
        this.minMap = minMap;
    }

    @Override
    public void visit(TTPVReader ttpvReader) {
        ttpvReader.setMaxMap(maxMap);
        ttpvReader.setMinMap(minMap);
    }


    @Override
    public void visit(TTDump ttDump) {
        ttDump.setMaxMap(maxMap);
        ttDump.setMaxMap(minMap);
    }

    @Override
    public void visit(TTLoad ttLoad) {
        ttLoad.setMaxMap(maxMap);
        ttLoad.setMinMap(minMap);
    }

}
