package net.chesstango.search.smart.debug;

import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.transposition.TranspositionBound;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class SearchTracker {

    public enum NodeType {ROOT, INTERIOR, TERMINAL, HORIZON, QUIESCENCE, Q_LEAF}

    private SearchNode currentNodeTracker;

    public void newNode(NodeType nodeType) {
        SearchNode newNode = new SearchNode();
        newNode.nodeType = nodeType;

        if (currentNodeTracker != null) {
            currentNodeTracker.addChild(newNode);
        }

        currentNodeTracker = newNode;
    }

    public void setDebugSearch(String fnString, int alpha, int beta) {
        currentNodeTracker.fnString = fnString;
        currentNodeTracker.alpha = alpha;
        currentNodeTracker.beta = beta;
    }

    public void setSelectedMove(Move currentMove) {
        currentNodeTracker.selectedMove = currentMove;
    }

    public void setValue(int value) {
        currentNodeTracker.value = value;
    }

    public void setStandingPat(Integer value) {
        currentNodeTracker.standingPat = value;
    }

    public void setZobristHash(long zobristHash) {
        currentNodeTracker.zobristHash = zobristHash;
    }

    public void trackReadTranspositionEntry(String tableName, long hashRequested, TranspositionEntry entry) {
        if (entry != null) {
            currentNodeTracker.transpositionOperations.add(new SearchNodeTT(SearchNodeTT.Type.READ,
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
        if (currentNodeTracker.zobristHash != hash) {
            throw new RuntimeException("currentNodeTracker.zobristHash != hash");
        }
        currentNodeTracker.transpositionOperations.add(new SearchNodeTT(SearchNodeTT.Type.WRITE,
                hash,
                tableName,
                hash,
                searchDepth,
                movesAndValue,
                transpositionBound
        ));
    }

    public void save() {
        if (currentNodeTracker.parent != null) {
            currentNodeTracker = currentNodeTracker.parent;
        }
    }

    public void reset() {
        currentNodeTracker = null;
    }

    public SearchNode getRootNode() {
        if (currentNodeTracker.parent != null) {
            throw new RuntimeException("Still searching?");
        }
        return currentNodeTracker;
    }


}
