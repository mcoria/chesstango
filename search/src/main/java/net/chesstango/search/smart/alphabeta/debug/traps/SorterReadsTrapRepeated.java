package net.chesstango.search.smart.alphabeta.debug.traps;

import net.chesstango.search.smart.alphabeta.debug.DebugNode;
import net.chesstango.search.smart.alphabeta.debug.DebugOperationTT;
import net.chesstango.search.smart.alphabeta.debug.DebugNodeTrap;
import net.chesstango.search.smart.transposition.TranspositionBound;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Captura Nodos con lecturas para ordenar; que coincidan en valor y al menos una de ellas sea EXACT
 *
 * @author Mauricio Coria
 */
public class SorterReadsTrapRepeated implements DebugNodeTrap {
    @Override
    public boolean test(DebugNode debugNode) {
        List<DebugOperationTT> sorterReads = debugNode.getSorterReads();

        Map<Integer, List<DebugOperationTT>> valueToDebugNodeTTMap = new HashMap<>();

        sorterReads.forEach(debugNodeTT -> {
            int ttValue = TranspositionEntry.decodeValue(debugNodeTT.getEntry().getMovesAndValue());
            List<DebugOperationTT> list = valueToDebugNodeTTMap.computeIfAbsent(ttValue, key -> new ArrayList<>());
            list.add(debugNodeTT);
        });


        for (Map.Entry<Integer, List<DebugOperationTT>> entry : valueToDebugNodeTTMap.entrySet()) {
            List<DebugOperationTT> entryList = entry.getValue();
            if (entryList.size() > 1 &&
                    entryList.stream().anyMatch(debugNodeTT -> TranspositionBound.EXACT.equals(debugNodeTT.getEntry().getTranspositionBound()))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void debug(DebugNode debugNode, PrintStream debugOut) {
        debugOut.print("ACA HAY UNA ENTRADA\n");
    }
}
