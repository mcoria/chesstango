package net.chesstango.search.smart.alphabeta.debug;

import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.transposition.TranspositionBound;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class SearchTracker {

    private DebugNode debugNode;

    public void newNode(DebugNode.SearchNodeType searchNodeType) {
        DebugNode newNode = new DebugNode();

        newNode.nodeType = searchNodeType;

        if (debugNode != null) {
            debugNode.addChild(newNode);
        }

        debugNode = newNode;
    }

    public void setDebugSearch(String fnString, int alpha, int beta) {
        debugNode.fnString = fnString;
        debugNode.alpha = alpha;
        debugNode.beta = beta;
    }

    public void setSelectedMove(Move currentMove) {
        debugNode.selectedMove = currentMove;
    }

    public void setValue(int value) {
        debugNode.value = value;
    }

    public void setStandingPat(Integer value) {
        debugNode.standingPat = value;
    }

    public void setZobristHash(long zobristHash) {
        debugNode.zobristHash = zobristHash;
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
        debugNode = null;
    }

    public DebugNode getRootNode() {
        if (debugNode.parent != null) {
            throw new RuntimeException("Still searching?");
        }
        return debugNode;
    }


}
