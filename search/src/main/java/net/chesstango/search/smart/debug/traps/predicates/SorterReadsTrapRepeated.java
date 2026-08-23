package net.chesstango.search.smart.debug.traps.predicates;

import net.chesstango.search.smart.debug.model.DebugNode;
import net.chesstango.search.smart.debug.model.DebugSortTT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static net.chesstango.search.Bound.EXACT;

/**
 * Captura Nodos con lecturas para ordenar; que coincidan en valor y al menos una de ellas sea EXACT
 *
 * @author Mauricio Coria
 */
public class SorterReadsTrapRepeated implements Predicate<DebugNode> {
    @Override
    public boolean test(DebugNode debugNode) {
        List<DebugSortTT> sorterReads = debugNode.getSorterReads();

        Map<Integer, List<DebugSortTT>> valueToDebugNodeTTMap = new HashMap<>();

        sorterReads.forEach(debugNodeTT -> {
            int ttValue = debugNodeTT.getEntry().getValue();
            List<DebugSortTT> list = valueToDebugNodeTTMap.computeIfAbsent(ttValue, key -> new ArrayList<>());
            list.add(debugNodeTT);
        });


        for (Map.Entry<Integer, List<DebugSortTT>> entry : valueToDebugNodeTTMap.entrySet()) {
            List<DebugSortTT> entryList = entry.getValue();
            if (entryList.size() > 1 &&
                    entryList.stream().anyMatch(debugNodeTT -> EXACT.equals(debugNodeTT.getEntry().getBound()))) {
                return true;
            }
        }

        return false;
    }
}
