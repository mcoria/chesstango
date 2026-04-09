package net.chesstango.search.smart.alphabeta.transposition.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.pv.PVCalculatorTransposition;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
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
    public void visit(PVCalculatorTransposition ttpvReader) {
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
