package net.chesstango.search.smart.features.debug.traps.predicates;

import net.chesstango.search.smart.features.debug.model.DebugNode;
import net.chesstango.search.smart.features.debug.model.DebugOperationTT;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
public class HorizonCutStandingPat implements Predicate<DebugNode> {

    private final List<DebugNode> candidates = new LinkedList<>();

    @Override
    public boolean test(DebugNode debugNode) {
        if (DebugNode.NodeTopology.HORIZON.equals(debugNode.getTopology())) {
            if (debugNode.getChildNodes().size() == 1) {
                DebugNode childNode = debugNode.getChildNodes().getFirst();
                if (DebugNode.NodeTopology.QUIESCENCE.equals(childNode.getTopology()) &&
                        childNode.getChildNodes().size() > 1 &&
                        Objects.equals(childNode.getStandingPat(), childNode.getValue())) {
                    candidates.add(debugNode);
                }
            }
        } else if (DebugNode.NodeTopology.INTERIOR.equals(debugNode.getTopology())
                && debugNode.getChildNodes().size() > 1
                && !debugNode.getEntryRead().isEmpty()) {
            Optional<DebugNode> candidateOpt = candidates.stream().filter(node -> node.getZobristHash() == debugNode.getZobristHash()).findFirst();
            if (candidateOpt.isPresent()) {
                DebugNode candidate = candidateOpt.get();
                if (!candidate.getEntryWrite().isEmpty()) {
                    DebugOperationTT candidateWriteTT = candidate.getEntryWrite().getFirst();
                    DebugOperationTT debugNodeReadTT = debugNode.getEntryRead().getFirst();
                    if (candidateWriteTT.getTableType().equals(debugNodeReadTT.getTableType()) &&
                            candidateWriteTT.getEntry().getMoveAndValue() == debugNodeReadTT.getEntry().getMoveAndValue()) {
                        DebugNode qNode = candidate.getChildNodes().getFirst();
                        for (int i = 0; i < Math.min(qNode.getChildNodes().size(), debugNode.getChildNodes().size()); i++) {
                            if (qNode.getChildNodes().get(i).getZobristHash() != debugNode.getChildNodes().get(i).getZobristHash()) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
