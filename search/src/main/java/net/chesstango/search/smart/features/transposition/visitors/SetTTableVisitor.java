package net.chesstango.search.smart.features.transposition.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.features.pv.TTPVReader;
import net.chesstango.search.smart.features.transposition.TTable;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionHeadMoveComparatorQ;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionTailMoveComparatorQ;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTable;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableQ;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableRoot;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableTerminal;
import net.chesstango.search.smart.features.transposition.listeners.ResetTranspositionTables;
import net.chesstango.search.smart.features.transposition.listeners.TTDump;
import net.chesstango.search.smart.features.transposition.listeners.TTLoad;

/**
 *
 * @author Mauricio Coria
 */
public class SetTTableVisitor implements Visitor {
    private final TTable maxMap;
    private final TTable minMap;
    private final TTable qMaxMap;
    private final TTable qMinMap;

    public SetTTableVisitor(TTable maxMap, TTable minMap, TTable qMaxMap, TTable qMinMap) {
        this.maxMap = maxMap;
        this.minMap = minMap;
        this.qMaxMap = qMaxMap;
        this.qMinMap = qMinMap;
    }

    @Override
    public void visit(ResetTranspositionTables resetTranspositionTables) {
        resetTranspositionTables.setMaxMap(maxMap);
        resetTranspositionTables.setMinMap(minMap);
        resetTranspositionTables.setQMaxMap(qMaxMap);
        resetTranspositionTables.setQMinMap(qMinMap);
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
        ttpvReader.setQMaxMap(qMaxMap);
        ttpvReader.setQMinMap(qMinMap);
    }

    @Override
    public void visit(TranspositionTableTerminal transpositionTableTerminal) {
        transpositionTableTerminal.setMaxMap(maxMap);
        transpositionTableTerminal.setMinMap(minMap);
        transpositionTableTerminal.setMaxQMap(qMaxMap);
        transpositionTableTerminal.setMinQMap(qMinMap);
    }

    @Override
    public void visit(TranspositionTable transpositionTable) {
        transpositionTable.setMaxMap(maxMap);
        transpositionTable.setMinMap(minMap);
    }

    @Override
    public void visit(TranspositionTableQ transpositionTableQ) {
        transpositionTableQ.setMaxMap(qMaxMap);
        transpositionTableQ.setMinMap(qMinMap);
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
    public void visit(TranspositionHeadMoveComparatorQ transpositionHeadMoveComparatorQ) {
        transpositionHeadMoveComparatorQ.setMaxMap(qMaxMap);
        transpositionHeadMoveComparatorQ.setMinMap(qMinMap);
    }

    @Override
    public void visit(TranspositionTailMoveComparator transpositionHeadMoveComparator) {
        transpositionHeadMoveComparator.setMaxMap(maxMap);
        transpositionHeadMoveComparator.setMinMap(minMap);
    }

    @Override
    public void visit(TranspositionTailMoveComparatorQ transpositionHeadMoveComparatorQ) {
        transpositionHeadMoveComparatorQ.setMaxMap(qMaxMap);
        transpositionHeadMoveComparatorQ.setMinMap(qMinMap);
    }

}
