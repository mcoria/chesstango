package net.chesstango.search.smart.features.debug.traps.predicates;

import net.chesstango.search.smart.alphabeta.filters.AlphaBetaHelper;
import net.chesstango.search.smart.features.debug.model.DebugNode;
import net.chesstango.search.smart.features.debug.model.DebugOperationTT;
import net.chesstango.search.smart.features.transposition.TranspositionBound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Captura Nodos con lecturas para ordenar; que coincidan en valor y al menos una de ellas sea EXACT
 *
 * @author Mauricio Coria
 */
public class SorterReadsTrapRepeated implements Predicate<DebugNode> {
    @Override
    public boolean test(DebugNode debugNode) {
        List<DebugOperationTT> sorterReads = debugNode.getSorterReads();

        Map<Integer, List<DebugOperationTT>> valueToDebugNodeTTMap = new HashMap<>();

        sorterReads.forEach(debugNodeTT -> {
            int ttValue = AlphaBetaHelper.decodeValue(debugNodeTT.getEntry().getMoveAndValue());
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
}
