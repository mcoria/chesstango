package net.chesstango.search.smart.alphabeta.transposition.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.pv.TTPVReader;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.smart.alphabeta.transposition.filters.*;
import net.chesstango.search.smart.alphabeta.transposition.listeners.ResetTranspositionTables;
import net.chesstango.search.smart.alphabeta.transposition.listeners.TTDump;
import net.chesstango.search.smart.alphabeta.transposition.listeners.TTLoad;

/**
 *
 * @author Mauricio Coria
 */
public class SetTTableVisitor implements Visitor {
    private final TTable maxMap;
    private final TTable minMap;

    public SetTTableVisitor(TTable maxMap, TTable minMap) {
        this.maxMap = maxMap;
        this.minMap = minMap;
    }

    @Override
    public void visit(ResetTranspositionTables resetTranspositionTables) {
        resetTranspositionTables.setMaxMap(maxMap);
        resetTranspositionTables.setMinMap(minMap);
    }

    @Override
    public void visit(TranspositionTableRoot transpositionTableRoot) {
        transpositionTableRoot.setMaxMap(maxMap);
        transpositionTableRoot.setMinMap(minMap);
    }

    @Override
    public void visit(TTPVReader ttpvReader) {
        ttpvReader.setMaxMap(maxMap);
        ttpvReader.setMinMap(minMap);
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

    @Override
    public void visit(TranspositionHeadMoveComparator transpositionHeadMoveComparator) {
        transpositionHeadMoveComparator.setMaxMap(maxMap);
        transpositionHeadMoveComparator.setMinMap(minMap);
    }

    @Override
    public void visit(TranspositionTailMoveComparator transpositionHeadMoveComparator) {
        transpositionHeadMoveComparator.setMaxMap(maxMap);
        transpositionHeadMoveComparator.setMinMap(minMap);
    }

}
