package net.chesstango.search.smart.alphabeta.debug;

import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluationType;
import net.chesstango.search.smart.transposition.TranspositionBound;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class SearchTracker {

    private DebugNode debugNode;

    public DebugNode newNode(DebugNode.SearchNodeType searchNodeType) {
        DebugNode newNode = new DebugNode();

        newNode.nodeType = searchNodeType;

        if (debugNode != null) {
            debugNode.childNodes.add(newNode);
            newNode.parent = debugNode;
        }

        debugNode = newNode;

        return debugNode;
    }

    public void trackReadTranspositionEntry(String tableName, long hashRequested, TranspositionEntry entry) {
        if (debugNode != null && entry != null) {
            debugNode.transpositionOperations.add(new DebugNodeTT(DebugNodeTT.TranspositionOperationType.READ,
                    hashRequested,
                    tableName,
                    entry.hash,
                    entry.searchDepth,
                    entry.movesAndValue,
                    entry.transpositionBound
            ));
        }
    }

    public void trackWriteTranspositionEntry(String tableName, long hash, int searchDepth, long movesAndValue, TranspositionBound transpositionBound) {
        if (debugNode.zobristHash != hash) {
            throw new RuntimeException("currentNodeTracker.zobristHash != hash");
        }
        debugNode.transpositionOperations.add(new DebugNodeTT(DebugNodeTT.TranspositionOperationType.WRITE,
                hash,
                tableName,
                hash,
                searchDepth,
                movesAndValue,
                transpositionBound
        ));
    }

    public void save() {
        if (debugNode.parent != null) {
            debugNode = debugNode.parent;
        }
    }

    public void reset() {
        if (Objects.nonNull(debugNode) && debugNode.parent != null) {
            throw new RuntimeException("Still searching?");
        }
        debugNode = null;
    }

    public DebugNode getRootNode() {
        if (Objects.nonNull(debugNode) && debugNode.parent != null) {
            throw new RuntimeException("Still searching?");
        }
        return debugNode;
    }
}
